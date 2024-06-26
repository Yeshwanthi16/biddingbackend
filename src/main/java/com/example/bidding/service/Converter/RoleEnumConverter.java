package com.example.bidding.service.Converter;

import jakarta.persistence.AttributeConverter;
import com.example.bidding.entity.user.RoleEnum;

public class RoleEnumConverter implements AttributeConverter<RoleEnum, String> {


    @Override
    public String convertToDatabaseColumn(RoleEnum roleEnum) {

        if (roleEnum == null) {
            return null;
        }

        String roleEnumString = null;

        switch (roleEnum) {
            case ROLE_ADMIN -> roleEnumString = "ADMIN";
            case ROLE_USER -> roleEnumString = "USER";
        }
        return roleEnumString;

    }

    @Override
    public RoleEnum convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }

        RoleEnum roleEnum = null;

        switch (s) {
            case "ADMIN" -> roleEnum = RoleEnum.ROLE_ADMIN;
            case "USER" -> roleEnum = RoleEnum.ROLE_USER;
        }
        return roleEnum;
    }
}
