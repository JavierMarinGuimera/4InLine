package tocados.marin.RESTServer.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This could be our controller to call the views.
 */
@Controller
@RequestMapping(path = "/")
public class ViewController {

    // We dont have methods yet. This can be created on the future.

    // Example:
    // @RequestMapping
    // public ModelAndView renderFooList() {
    // ModelAndView mav = new ModelAndView("foo/list");
    // mav.addObject("foos", fooService.getFoos());
    // return mav;
    // }
}
