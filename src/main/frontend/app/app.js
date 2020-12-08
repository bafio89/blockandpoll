import React from "react";
import ReactDOM from 'react-dom';
import {HashRouter, Route, Switch} from "react-router-dom";
import CreatePoll from "./CreatePoll";

ReactDOM.render(
    <HashRouter>
      <Switch>
        <Route path="/" component={CreatePoll}/>
      </Switch>
    </HashRouter>,
    document.getElementById('react'));
