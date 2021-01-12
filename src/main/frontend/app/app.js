import React from "react";
import ReactDOM from 'react-dom';
import {HashRouter, Route, Switch} from "react-router-dom";
import Home from "./home";
import CreatePoll from "./CreatePoll";
import Poll from "./Poll";

ReactDOM.render(
    <HashRouter>
      <Switch>
        <Route exact path="/" component={Home}/>
        <Route path="/createpoll/" component={CreatePoll}/>
        <Route path="/poll/:id" component={Poll}/>
      </Switch>
    </HashRouter>,
    document.getElementById('react'));
