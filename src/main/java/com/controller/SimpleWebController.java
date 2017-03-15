package com.controller;

import com.Util.CheckCertsUtil;
import com.model.Validate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SimpleWebController {

  @RequestMapping(value = "/validateForm", method = RequestMethod.GET)
  public String customerForm(Model model) {
    model.addAttribute("validateForm", new Validate());
    return "validateForm";
  }

  @RequestMapping(value = "/validateForm", method = RequestMethod.POST)
  public String submit(@ModelAttribute Validate validate, Model model) throws Exception {
    model.addAttribute("validateForm", validate);
    String validUntil = CheckCertsUtil.checkCerts(validate.getUrl());
    validate.setValidUntil(validUntil);
    return "result";
  }
}
