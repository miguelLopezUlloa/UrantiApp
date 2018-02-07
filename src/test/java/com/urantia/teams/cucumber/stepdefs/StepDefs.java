package com.urantia.teams.cucumber.stepdefs;

import com.urantia.teams.UranappApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = UranappApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
