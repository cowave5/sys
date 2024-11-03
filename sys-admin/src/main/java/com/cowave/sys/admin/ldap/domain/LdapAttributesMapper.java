/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.ldap.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
public class LdapAttributesMapper implements AttributesMapper<LdapUser> {

    private final LdapConfig ldapConfig;

    @Override
    public LdapUser mapFromAttributes(Attributes attributes) throws NamingException {
        LdapUser ldapUser = new LdapUser();
        NamingEnumeration<? extends Attribute> attributeEnum = attributes.getAll();
        while (attributeEnum.hasMore()) {
            Attribute attribute = attributeEnum.next();
            ldapUser.valueUserAccount(attribute, ldapConfig);
            ldapUser.valueUserName(attribute, ldapConfig);
            ldapUser.valueUserEmail(attribute, ldapConfig);
            ldapUser.valueUserPhone(attribute, ldapConfig);
            ldapUser.valueUserPost(attribute, ldapConfig);
            ldapUser.valueDepartment(attribute, ldapConfig);
            ldapUser.valueUserLeader(attribute, ldapConfig);
            ldapUser.valueUserInfo(attribute, ldapConfig);
        }
        return ldapUser;
    }
}
