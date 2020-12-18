import React from "react";
import ReactDOM from 'react-dom';
import {HashRouter, Route, Switch} from "react-router-dom";
import Home from "./home";
import CreatePoll from "./CreatePoll";

ReactDOM.render(
    <HashRouter>
      <Switch>
        <Route path="/home" component={Home}/>
        <Route path="/createpoll/" component={CreatePoll}/>
      </Switch>
    </HashRouter>,
    document.getElementById('react'));
