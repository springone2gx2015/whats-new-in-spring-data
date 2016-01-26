/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.web;

import static org.springframework.validation.ValidationUtils.*;

import example.Password;
import example.User;
import example.UserManagement;
import example.Username;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

/**
 * A sample controller implementation to showcase Spring Data web support:
 * <ol>
 * <li>Automatic population of a {@link Pageable} instance as controller method argument. This is achieved by the
 * automatic activation of {@link EnableSpringDataWebSupport} and in turn its registration of a
 * {@link PageableHandlerMethodArgumentResolver}.</li>
 * <li>Usage of proxy-backed interfaces to bind request parameters.</li>
 * </ol>
 * 
 * @author Oliver Gierke
 */
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
@RequestMapping("/users")
class UserController {

	private final UserManagement userManagement;
	private final ProjectionFactory projections;

	/**
	 * Equips the model with a {@link Page} of {@link User}s. Spring Data automatically populates the {@link Pageable}
	 * from request data according to the setup of {@link PageableHandlerMethodArgumentResolver}. Note how the defaults
	 * can be tweaked by using {@link PageableDefault}.
	 * 
	 * @param pageable will never be {@literal null}.
	 * @return
	 */
	@ModelAttribute("users")
	public Page<User> users(@PageableDefault(size = 5) Pageable pageable) {
		return userManagement.findAll(pageable);
	}

	/**
	 * Registers a new {@link User} for the data provided by the given {@link UserForm}. Note, how an interface is used to
	 * bind request parameters.
	 * 
	 * @param form the request data bound to the {@link UserForm} instance.
	 * @param binding the result of the binding operation.
	 * @param model the Spring MVC {@link Model}.
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Object register(UserForm userForm, BindingResult binding, Model model) {

		// TODO: 02 - Inbound projections

		userForm.validate(binding, userManagement);

		if (binding.hasErrors()) {
			return "users";
		}

		userManagement.register(new Username(userForm.getUsername()), Password.raw(userForm.getPassword()));

		RedirectView redirectView = new RedirectView("redirect:/users");
		redirectView.setPropagateQueryParams(true);

		return redirectView;
	}

	/**
	 * Populates the {@link Model} with the {@link UserForm} automatically created by Spring Data web components. It will
	 * create a {@link Map}-backed proxy for the interface.
	 * 
	 * @param model will never be {@literal null}.
	 * @param userForm will never be {@literal null}.
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String listUsers(Model model, UserForm userForm) {

		model.addAttribute("userForm", userForm);

		return "users";
	}

	/**
	 * Returns the first page of {@link User}s as JSON representation. Note how we leverage the {@link ProjectionFactory}
	 * to wrap each result into the {@link UsernamesOnly} projection.
	 * 
	 * <pre>
	 * curl -H "Content-type: application/json" -H "Accept: application/json" http://localhost:8080/users 
	 * 
	 * http --json http://localhost:8080/users
	 * </pre>
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<? extends Object> getUsers() {

		// TODO: 01 - Projections

		return userManagement.findAll(new PageRequest(0, 10))//
				.map(user -> projections.createProjection(UsernamesOnly.class, user))//
				.getContent();
	}

	/**
	 * An interface to represent the form to be used
	 *
	 * @author Oliver Gierke
	 */
	interface UserForm {

		String getUsername();

		String getPassword();

		String getRepeatedPassword();

		/**
		 * Validates the {@link UserForm}.
		 * 
		 * @param errors
		 * @param userManagement
		 */
		default void validate(BindingResult errors, UserManagement userManagement) {

			rejectIfEmptyOrWhitespace(errors, "username", "user.username.empty");
			rejectIfEmptyOrWhitespace(errors, "password", "user.password.empty");
			rejectIfEmptyOrWhitespace(errors, "repeatedPassword", "user.repeatedPassword.empty");

			if (!getPassword().equals(getRepeatedPassword())) {
				errors.rejectValue("repeatedPassword", "user.password.no-match");
			}

			try {

				userManagement.findByUsername(new Username(getUsername()))
						.ifPresent(user -> errors.rejectValue("username", "user.username.exists"));

			} catch (IllegalArgumentException o_O) {
				errors.rejectValue("username", "user.username.invalidFormat");
			}
		}
	}

	/**
	 * Projection interface
	 *
	 * @author Oliver Gierke
	 */
	interface UsernamesOnly {

		@Value("#{target.username.toString()}")
		String getUsername();
	}
}
