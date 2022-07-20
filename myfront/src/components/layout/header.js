import React from "react";
// import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

import '../../css/layout.css';
import { Link } from 'react-router-dom/cjs/react-router-dom';
import { useDispatch, useSelector } from "react-redux";
import actions from '../../redux/reducers/users/users.action';
import { useHistory } from 'react-router-dom';
import { useState } from "react";
import { useEffect } from "react";
import axios from "axios";
import { useCookies } from 'react-cookie';


function Header() {
    const { userid, rememberme } = useSelector(state => state.users);
    const dispatch = useDispatch();
    const history = useHistory();
    const [cookies, setCookie, removeCookie] = useCookies();


    const logout = () => {
        // cookieDelete();
        dispatch(actions.userLogout());
        removeCookie('logged', { path: '/' });
        removeCookie('refreshToken', { path: '/' });
        removeCookie('accessToken', { path: '/' });

        delete axios.defaults.headers.common["Authorization"];
        history.push('/user/sign-in');
        history.go(0);
    }



    return (

        <nav className="navbar">
            <div className="content">
                <Link to="/" className="home">Ryu App</Link>
            </div>
            {
                userid === null
                    ? 
                    <div className="user">
                        <Link to="/user/sign-in"><button className="btn  btn-primary">Sign in</button></Link>
                        <Link to="/user/sign-up"><button className="btn  btn-primary">Sign up</button></Link>
                    </div>
                    :
                    <div className="user">
                        <p>
                            {userid}님 환영합니다.
                            <button className="btn btn-warning btn-logout" onClick={logout}>LOGOUT</button>
                        </p>
                    </div>
            }
        </nav>
    )
}
export default Header;