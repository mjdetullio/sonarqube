/*
 * SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.api.server.authentication;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * User information provided by the Identity Provider to be register into the platform.
 *
 * @since 5.4
 */
@Immutable
public final class UserIdentity {

  private final String providerId;
  private final String login;
  private final String name;
  private final String email;

  private UserIdentity(Builder builder) {
    this.providerId = builder.providerId;
    this.login = builder.login;
    this.name = builder.name;
    this.email = builder.email;
  }

  /**
   * Non-blank user ID, unique for the related {@link IdentityProvider}.
   * If two {@link IdentityProvider}define two users with the same ID, then users are considered as different.
   */
  public String getProviderId() {
    return providerId;
  }

  /**
   * Non-blank user login, unique for the SonarQube platform.
   * If two {@link IdentityProvider}define two users with the same login, then users are considered as identical.
   */
  public String getLogin() {
    return login;
  }

  /**
   * Non-blank display name. Uniqueness is not mandatory, even it's recommended for easier search of users
   * in webapp.
   */
  public String getName() {
    return name;
  }

  /**
   * Optional non-blank email. If defined, then it must be unique among all the users defined by all
   * {@link IdentityProvider}. If not unique, then authentication will fail.
   */
  @CheckForNull
  public String getEmail() {
    return email;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String providerId;
    private String login;
    private String name;
    private String email;

    private Builder() {
    }

    /**
     * @see UserIdentity#getProviderId()
     */
    public Builder setProviderId(String providerId) {
      this.providerId = providerId;
      return this;
    }

    /**
     * @see UserIdentity#getLogin() ()
     */
    public Builder setLogin(String login) {
      this.login = login;
      return this;
    }

    /**
     * @see UserIdentity#getName()
     */
    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    /**
     * @see UserIdentity#getEmail()
     */
    public Builder setEmail(@Nullable String email) {
      this.email = email;
      return this;
    }

    public UserIdentity build() {
      validateProviderId(providerId);
      validateLogin(login);
      validateName(name);
      validateEmail(email);
      return new UserIdentity(this);
    }

    private static void validateProviderId(String providerId){
      checkArgument(isNotBlank(providerId), "User id must not be blank");
      checkArgument(providerId.length() <= 255 && providerId.length() >= 3, "User id size is incorrect (Between 3 and 255 characters)");
    }

    private static void validateLogin(String login){
      checkArgument(isNotBlank(login), "User login must not be blank");
      checkArgument(login.length() <= 255 && login.length() >= 3, "User login size is incorrect (Between 3 and 255 characters)");
    }

    private static void validateName(String name){
      checkArgument(isNotBlank(name), "User name must not be blank");
      checkArgument(name.length() <= 200, "User name size is too big (200 characters max)");
    }

    private static void validateEmail(@Nullable String email){
      checkArgument(email == null || email.length() <= 100, "User email size is too big (100 characters max)");
    }
  }
}
