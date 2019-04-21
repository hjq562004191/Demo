package com.map.web.service.impl;

import com.map.web.service.UtilValidate;
import org.springframework.stereotype.Service;

@Service
public class UtilValidateImpl implements UtilValidate {
    @Override
    public boolean isEmpty(String token) {
            if (token == null|| token.equals(""))
                return true;
            else
                return false;
    }
}
