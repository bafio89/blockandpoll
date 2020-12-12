import React from "react";
import ReactDOM from 'react-dom';
import {HashRouter, Route, Switch} from "react-router-dom";
import ShowPoll from "./ShowPoll";

ReactDOM.render(
    <HashRouter>
      <Switch>
        {/*<Route path="/" component={CreatePoll}/>*/}
        <Route path="/" component={ShowPoll}/>
      </Switch>
    </HashRouter>,
    document.getElementById('react'));
