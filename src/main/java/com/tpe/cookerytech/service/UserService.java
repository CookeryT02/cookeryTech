package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.domain.enums.RoleType;
import com.tpe.cookerytech.dto.request.AdminUserUpdateRequest;
import com.tpe.cookerytech.dto.request.RegisterRequest;
import com.tpe.cookerytech.dto.request.UpdatePasswordRequest;
import com.tpe.cookerytech.dto.request.UserUpdateRequest;
import com.tpe.cookerytech.dto.response.UserResponse;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.ConflictException;
import com.tpe.cookerytech.exception.PasswordValidator;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.ProductMapper;
import com.tpe.cookerytech.mapper.UserMapper;
import com.tpe.cookerytech.repository.OfferRepository;
import com.tpe.cookerytech.repository.ProductRepository;
import com.tpe.cookerytech.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import com.tpe.cookerytech.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;



    public UserService(UserRepository userRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder, UserMapper userMapper, ProductRepository productRepository, ProductMapper productMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(
                        String.format(ErrorMessage.USER_NOT_FOUND_EXCEPTION, email)));

        return user;
    }

    public UserResponse saveUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(
                    String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,
                            registerRequest.getEmail())
            );

        }


        Role role = roleService.findByType(RoleType.ROLE_CUSTOMER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User user = userMapper.userRequestToUser(registerRequest);
        String password = registerRequest.getPassword();

        if (!isPasswordValid(password)) {
            throw new BadRequestException(String.format(ErrorMessage.USER_PASSWORD_CONTROL));
        } else {
            user.setPasswordHash(encodedPassword);
        }

        user.setRoles(roles);
        user.setResetPasswordCode(null);
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(null);
        user.setStatus((byte) 0);

        userRepository.save(user);


        return userMapper.userToUserResponse(user);

    }

    public boolean isPasswordValid(String password) {
        return PasswordValidator.containsSpecialChars(password);
    }


    public UserResponse updateUserPassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = getCurrentUser();

        if (user.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // !!! Forma girilen OldPassword doğru mu
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPasswordHash())) {
            throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED_MESSAGE);
        }
        // !!! yeni gelen şifreyi encode edilecek
        String hashedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        user.setPasswordHash(hashedPassword);
        userRepository.save(user);
        return userMapper.userToUserResponse(user);
    }

        //*****************************Yardimci Methodlar***************************************




        public UserResponse getPrincipal () {
            User user = getCurrentUser();
            UserResponse userResponse = userMapper.userToUserResponse(user);
            return userResponse;
        }

        public UserResponse updateUser (UserUpdateRequest userUpdateRequest){

            User user = getCurrentUser();

            if (user.getBuiltIn()) {
                throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
            }

            boolean emailExist = userRepository.existsByEmail(userUpdateRequest.getEmail());

            if (emailExist && !userUpdateRequest.getEmail().equals(user.getEmail())) {
                throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, userUpdateRequest.getEmail()));
            }

            user.setFirstName(userUpdateRequest.getFirstName());
            user.setLastName(userUpdateRequest.getLastName());
            user.setEmail(userUpdateRequest.getEmail());
            user.setPhone(userUpdateRequest.getPhone());
            user.setAddress(userUpdateRequest.getAddress());
            user.setCity(userUpdateRequest.getCity());
            user.setCountry(userUpdateRequest.getCountry());
            user.setBirthDate(userUpdateRequest.getBirthDate());
            user.setTaxNo(userUpdateRequest.getTaxNo());
            user.setUpdateAt(LocalDateTime.now());

            userRepository.save(user);


            return userMapper.userToUserResponse(user);

        }


    public Page<UserResponse> getUserPage(String q, Pageable pageable) {

        Page<User> userPage = userRepository.getUsers(q,pageable);

        return getUserResponsePage(userPage);
    }

    //*****************************Yardimci Methodlar***************************************
    public User getCurrentUser() {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
        User user = getUserByEmail(email);
        return user;
    }





    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));

        return userMapper.userToUserResponse(user);

    }
    public User getUserEntityById(Long id) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));

        return user;

    }


    private Page<UserResponse> getUserResponsePage(Page<User> userPage) {

        return userPage.map(user -> userMapper.userToUserResponse(user));
    }


    public UserResponse updateUserById(Long id, AdminUserUpdateRequest adminUserUpdateRequest) {

        User user = getUserEntityById(id);

        Set<Role> roleControl = getCurrentUser().getRoles();
        roleControl.forEach(r ->
                {
                    if (r.getType().equals(RoleType.ROLE_ADMIN)) {
                        userSetting(user, adminUserUpdateRequest);

                    } else if (r.getType().equals(RoleType.ROLE_SALES_MANAGER)) {
                        user.getRoles().forEach(role -> {
                            if (role.getType().equals(RoleType.ROLE_ADMIN) || role.getType().equals(RoleType.ROLE_PRODUCT_MANAGER)) {
                                throw new BadRequestException(String.format(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE));

                            } else if (role.getType().equals(RoleType.ROLE_SALES_SPECIALIST) || role.getType().equals(RoleType.ROLE_CUSTOMER)) {
                                userSetting(user, adminUserUpdateRequest);
                            }
                        });
                    } else if (r.getType().equals(RoleType.ROLE_SALES_SPECIALIST)) {
                        user.getRoles().forEach(role -> {
                            if (role.getType().equals(RoleType.ROLE_ADMIN) || role.getType().equals(RoleType.ROLE_PRODUCT_MANAGER) || role.getType().equals(RoleType.ROLE_SALES_MANAGER)) {
                                throw new BadRequestException(String.format(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE));

                            } else if (role.getType().equals(RoleType.ROLE_SALES_SPECIALIST) || role.getType().equals(RoleType.ROLE_CUSTOMER)) {
                                userSetting(user, adminUserUpdateRequest);
                            }
                        });
                    } else {
                        throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
                    }
                }
        );
        return userMapper.userToUserResponse(user);

    }

    private void userSetting(User user, AdminUserUpdateRequest adminUserUpdateRequest) {
        //builtIn kontrol
        if (user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
        //email kontrol
        if(adminUserUpdateRequest.getEmail()==null){
            adminUserUpdateRequest.setEmail(user.getEmail());
        }else {
            boolean emailExist = userRepository.existsByEmail(adminUserUpdateRequest.getEmail());
            if (emailExist && !(adminUserUpdateRequest.getEmail().equals(user.getEmail()))) {
                throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, adminUserUpdateRequest.getEmail()));
            }
        }
        if (adminUserUpdateRequest.getPassword() == null) {
            adminUserUpdateRequest.setPassword(user.getPasswordHash());
        } else {
            String encodedNewPassword = passwordEncoder.encode(adminUserUpdateRequest.getPassword());
            adminUserUpdateRequest.setPassword(encodedNewPassword);
        }

        user.setFirstName(adminUserUpdateRequest.getFirstName());
        user.setLastName(adminUserUpdateRequest.getLastName());
        user.setEmail(adminUserUpdateRequest.getEmail());
        user.setPasswordHash(adminUserUpdateRequest.getPassword());
        user.setPhone(adminUserUpdateRequest.getPhone());
        user.setBirthDate(adminUserUpdateRequest.getBirthDate());
        user.setUpdateAt(LocalDateTime.now());
        user.setCity(adminUserUpdateRequest.getCity());
        user.setCountry(adminUserUpdateRequest.getCountry());
        user.setTaxNo(adminUserUpdateRequest.getTaxNo());
        user.setAddress(adminUserUpdateRequest.getAddress());
        user.setStatus(adminUserUpdateRequest.getStatus());
        user.setBuiltIn(adminUserUpdateRequest.getBuiltIn());

        Set<String> roles = adminUserUpdateRequest.getRoles();
        Set<Role> newRole = convertRoles(roles);

        user.setRoles(newRole);
        userRepository.save(user);
    }

    private Set<Role> convertRoles(Set<String> roles) {
        Set<Role> newRoles = new HashSet<>();

        if (roles == null) {
            Role userRole = roleService.findByType(RoleType.ROLE_CUSTOMER);
            newRoles.add(userRole);
        } else {
            roles.forEach(roleStr -> {
                if (roleStr.equalsIgnoreCase(RoleType.ROLE_ADMIN.getName())) {
                    Role adminRole = roleService.findByType(RoleType.ROLE_ADMIN);
                    newRoles.add(adminRole);
                } else if (roleStr.equalsIgnoreCase(RoleType.ROLE_SALES_MANAGER.getName())) {
                    Role salesManagerRole = roleService.findByType(RoleType.ROLE_SALES_MANAGER);
                    newRoles.add(salesManagerRole);
                } else if (roleStr.equalsIgnoreCase(RoleType.ROLE_SALES_SPECIALIST.getName())) {
                    Role salesSpecialistRole = roleService.findByType(RoleType.ROLE_SALES_SPECIALIST);
                    newRoles.add(salesSpecialistRole);
                } else if (roleStr.equalsIgnoreCase(RoleType.ROLE_PRODUCT_MANAGER.getName())) {
                    Role productManagerRole = roleService.findByType(RoleType.ROLE_PRODUCT_MANAGER);
                    newRoles.add(productManagerRole);
                } else if (roleStr.equalsIgnoreCase(RoleType.ROLE_CUSTOMER.getName())) {
                    Role customerRole = roleService.findByType(RoleType.ROLE_CUSTOMER);
                    newRoles.add(customerRole);
                } else {
                    throw new BadRequestException(ErrorMessage.ROLE_NOT_FOUND_EXCEPTION);
                }
            });
        }
        return newRoles;
    }

    @Transactional
    public void deleteCurrentUser(String password) {

        User user = getCurrentUser();

        // built-in (kullanıcının offers tablosunda kaydı varsa silinemeyecek)
        if (user.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }


        boolean passwordsMatch = BCrypt.checkpw(password, user.getPasswordHash());
        if (!passwordsMatch){
            throw new BadRequestException(ErrorMessage.WRONG_PASSWORD_EXCEPTION);
        }

        userRepository.deleteByEmail(user.getEmail());
        // SecurityContextHolder.clearContext(); (oturumu kapatmak için)

    }

    @Transactional
    public UserResponse deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.PRINCIPAL_FOUND_MESSAGE)));

        if (OfferRepository.existByUser(user)) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        if (user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        User currentUser = getCurrentUser();
        Set<Role> currentUserRole = currentUser.getRoles();
        // Check if the current user has the necessary role or permission to delete the target user.
        currentUserRole.forEach(r -> {
            if (r.getType().equals(RoleType.ROLE_ADMIN)) {
                userRepository.delete(user);

            } else if (r.getType().equals(RoleType.ROLE_SALES_MANAGER)) {
                user.getRoles().forEach(role -> {
                    if (role.getType().equals(RoleType.ROLE_ADMIN) ||
                    role.getType().equals(RoleType.ROLE_PRODUCT_MANAGER)) {
                        throw new BadRequestException(String.format(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE));

                    } else if (role.getType().equals(RoleType.ROLE_SALES_SPECIALIST) ||
                    role.getType().equals(RoleType.ROLE_CUSTOMER)) {
                        userRepository.delete(user);
                    }
                });
            } else if (r.getType().equals(RoleType.ROLE_SALES_SPECIALIST)) {
                user.getRoles().forEach(role -> {
                    if (role.getType().equals(RoleType.ROLE_ADMIN) ||
                    role.getType().equals(RoleType.ROLE_PRODUCT_MANAGER) ||
                    role.getType().equals(RoleType.ROLE_SALES_MANAGER)) {
                        throw new BadRequestException(String.format(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE));

                    } else if (role.getType().equals(RoleType.ROLE_SALES_SPECIALIST)
                            || role.getType().equals(RoleType.ROLE_CUSTOMER)) {
                        userRepository.delete(user);
                    }
                });
            } else {
                throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);

            }
        });

        return userMapper.userToUserResponse(user);
    }


    public User getUserForRoleAuthUser() {

        String email = SecurityUtils.getCurrentUserLogin().orElse(null);

        if (email.equals("anonymousUser")) {
            return null;
        }

        User user = getUserByEmail(email);
        if ( user == null ) {
            throw new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE);
        }

        return user;


    }

    public User getAllUsers() {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
        User user = getUserByEmail(email);
        return user;
    }


}

