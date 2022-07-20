import React from "react";
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom';


// COMPONENT
import UserSignUp from "./UserSignUp";
import UserSignIn from './UserSignIn';
import Home from './../home';
import { useSelector } from 'react-redux';

function User() {

    return (
        <Router>
            <Switch>
                <Route exact path={`/user/sign-up`} component={UserSignUp} />
                <Route exact path={`/user/sign-in`} component={UserSignIn} />
            </Switch>
        </Router>
    )
}

export default User;