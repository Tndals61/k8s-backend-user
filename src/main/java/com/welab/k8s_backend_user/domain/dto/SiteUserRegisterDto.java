package com.welab.k8s_backend_user.domain.dto;

import com.welab.k8s_backend_user.domain.SiteUser;
import com.welab.k8s_backend_user.secret.hash.SecureHashUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SiteUserRegisterDto {
    @NotBlank(message = "Field ID is compulsory")
    private String userId;

    @NotBlank(message = "Field Password is compulsory")
    private String password;

    @NotBlank(message = "Field Phone Number is compulsory")
    private String phoneNumber;

    public SiteUser toEntity(){
        SiteUser siteUser = new SiteUser();

        siteUser.setUserId(this.userId);
        siteUser.setPassword(SecureHashUtils.hash(this.password));
        siteUser.setPhoneNumber(this.phoneNumber);

        return siteUser;
    }

}
