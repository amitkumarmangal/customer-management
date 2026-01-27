package com.business.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "api-gateway")
public interface AccountUtil {
    @DeleteMapping("/rest/api/account/v1/remove/{id}")
    void removeAccounts(@RequestHeader("Authorization") String token, @PathVariable("id") Long accountId);

    @DeleteMapping("/rest/api/account/v1/{id}")
    void softDeleteAccounts(@RequestHeader("Authorization") String token,@PathVariable("id") Long accountId);
}
