import React from "react";
import ReactDOM from 'react-dom';
import {HashRouter, Route, Switch} from "react-router-dom";
import Home from "./home";

ReactDOM.render(
    <HashRouter>
      <Switch>
        {/*<Route path="/" component={CreatePoll}/>*/}
        <Route path="/" component={Home}/>
      </Switch>
    </HashRouter>,
    document.getElementById('react'));
