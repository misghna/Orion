package com.sesnu.orion.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sesnu.orion.web.jsonview.Views;
import com.sesnu.orion.web.model.AjaxResponseBody;
import com.sesnu.orion.web.model.SearchCriteria;
import com.sesnu.orion.web.model.User;

@RestController
public class AjaxController {

	List<User> users;

	// @ResponseBody, not necessary, since class is annotated with @RestController
	// @RequestBody - Convert the json data into object (SearchCriteria) mapped by field name.
	// @JsonView(Views.Public.class) - Optional, limited the json data display to client.
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/search/api/getSearchResult")
	public AjaxResponseBody getSearchResultViaAjax(@RequestBody SearchCriteria search) {

		AjaxResponseBody result = new AjaxResponseBody();

		if (isValidSearchCriteria(search)) {
			List<User> users = findByUserNameOrEmail(search.getUsername(), search.getEmail());

			if (users.size() > 0) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(users);
			} else {
				result.setCode("204");
				result.setMsg("No user!");
			}

		} else {
			result.setCode("400");
			result.setMsg("Search criteria is empty!");
		}

		//AjaxResponseBody will be converted into json format and send back to client.
		return result;

	}

	private boolean isValidSearchCriteria(SearchCriteria search) {

		boolean valid = true;

		if (search == null) {
			valid = false;
		}

		if ((StringUtils.isEmpty(search.getUsername())) && (StringUtils.isEmpty(search.getEmail()))) {
			valid = false;
		}

		return valid;
	}


	// Simulate the search function
	private List<User> findByUserNameOrEmail(String username, String email) {

		List<User> result = new ArrayList<User>();

		for (User user : users) {

//			if ((!StringUtils.isEmpty(username)) && (!StringUtils.isEmpty(email))) {
//
//				if (username.equals(user.getUsername()) && email.equals(user.getEmail())) {
//					result.add(user);
//					continue;
//				} else {
//					continue;
//				}
//
//			}
//			if (!StringUtils.isEmpty(username)) {
//				if (username.equals(user.getUsername())) {
//					result.add(user);
//					continue;
//				}
//			}
//
//			if (!StringUtils.isEmpty(email)) {
//				if (email.equals(user.getEmail())) {
//					result.add(user);
//					continue;
//				}
//			}

		}

		return result;

	}
}
