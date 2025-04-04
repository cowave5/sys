/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.blog.api.controller;

import com.cowave.commons.client.http.asserts.AssertsException;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.sys.blog.api.entity.LoginUser;
import com.cowave.sys.blog.configuration.LdapConfiguration;
import com.cowave.sys.blog.utils.LdapUserNameMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.naming.directory.SearchControls;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.cowave.commons.client.http.constants.HttpCode.UNAUTHORIZED;

/**
 * 认证
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Random rand = new Random();

    private final BearerTokenService bearerTokenService;

    private final LdapConfiguration ldapConfiguration;

    private final ObjectProvider<DirContextAuthenticationStrategy> dirContextAuthenticationStrategy;

    /**
     * 登录
     */
    @RequestMapping("/login")
    public Response<Void> login(@Validated LoginUser loginUser) throws IOException {
        LdapTemplate ldapTemplate = getLdapTemplate();
        String filter = "(&(objectClass=person)(sAMAccountName=" + loginUser.getUserAccount() + "))";
        boolean isAuthenticated = ldapTemplate.authenticate("", filter, loginUser.getPassWord());
        HttpAsserts.isTrue(isAuthenticated, UNAUTHORIZED, "用户名或密码错误");

        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setUsername(loginUser.getUserAccount());
        bearerTokenService.simpleAssignToken(userDetails);
        // 界面约定
        Access.setCookie("blog-userName", loginUser.getUserAccount());

        List<String> list = ldapTemplate.search(
                ldapConfiguration.getUserDn(), filter, SearchControls.SUBTREE_SCOPE, new LdapUserNameMapper());
        if(CollectionUtils.isNotEmpty(list)){
            avatarGenerate(list.get(0), loginUser.getUserAccount());
        }
        return Response.success();
    }

    private LdapTemplate getLdapTemplate(){
        LdapContextSource source = new LdapContextSource();
        dirContextAuthenticationStrategy.ifUnique(source::setAuthenticationStrategy);
        PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        try{
            propertyMapper.from(ldapConfiguration.getLdapUser()).to(source::setUserDn);
            propertyMapper.from(ldapConfiguration.getLdapPasswd()).to(source::setPassword);
            propertyMapper.from(ldapConfiguration.anonymousReadOnly()).to(source::setAnonymousReadOnly);
            propertyMapper.from(ldapConfiguration.getBaseDn()).to(source::setBase);
            propertyMapper.from(ldapConfiguration.determineUrls()).to(source::setUrls);
            propertyMapper.from(ldapConfiguration.getEnvironment()).to(
                    baseEnvironment -> source.setBaseEnvironmentProperties(Collections.unmodifiableMap(baseEnvironment)));
            source.afterPropertiesSet();
        }catch(Exception e){
            throw new AssertsException(e, "ldap.invalid");
        }
        return new LdapTemplate(source);
    }

    private void avatarGenerate(String userName, String userAccount) throws IOException {
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(userAccount)){
            return;
        }
        File avatar = new File("public/avatar/" + userAccount + ".jpg");
        if(avatar.exists()){
            return;
        }
        String text = userName.substring(0, 1);
        int width = 128;
        int height = 128;
        // Create a buffered image with a specific size and type
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        // Enable antialiasing for text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Draw a white background
        Color backgroundColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, width, height);
        // Set the font to use for drawing the string
        double bgLuminance = 0.299 * backgroundColor.getRed() + 0.587 * backgroundColor.getGreen() + 0.114 * backgroundColor.getBlue();
        Color textColor;
        if (bgLuminance < 128) {
            textColor = Color.WHITE;
        } else {
            textColor = Color.BLACK;
        }
        Font font = new Font("宋体", Font.BOLD, 64);
        g2d.setFont(font);
        g2d.setColor(textColor);

        // Get font metrics for the string to be drawn
        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(text, context);
        // Calculate the coordinates to center the text
        int x = (width - (int) bounds.getWidth()) / 2;
        int y = (height - (int) bounds.getHeight()) / 2 - (int) bounds.getY();
        // Draw the string
        g2d.drawString(text, x, y);
        // Save the image as JPG
        File avatarDir = new File("public/avatar");
        if (avatarDir.exists() || avatarDir.mkdirs()) {
            ImageIO.write(image, "jpg", avatar);
        }
        // Dispose the graphics object
        g2d.dispose();
    }
}
