import React, { useState } from "react";
import axios from 'axios';
import actions from '../../redux/reducers/users/users.action';
import { useSelector, useDispatch } from 'react-redux';

import { useHistory } from "react-router-dom";
import { useEffect } from "react";
import { Link } from 'react-router-dom/cjs/react-router-dom';


import { now } from "lodash";
import { Cookies } from 'react-cookie';
import { useCookies } from 'react-cookie';




function UserSignIn() {

    const dispatch = useDispatch();
    const { userid } = useSelector(state => state.users);
    const histoty = useHistory();
    const [user, setUser] = useState({
        userid: '',
        password: '',
    });
    const [check, setCheck] = useState(true);
    const [loading, setLoading] = useState(true);
    const [cookies, setCookie, removeCookie] = useCookies();

    const userCheck = () => {
        if (cookies.logged != null) {
            setLoading(true);
        } else {
            setLoading(false)
        }
    }

    useEffect(() => {
        userCheck();
    }, [])

    const loginSubmit = (e) => {
        e.preventDefault();
        const regExp = /[a-zA-Z]/g;

        if (3 < user.userid.length < 9 && user.password.length < 8) {
            alert('입력이 올바르지 않습니다.')
        } else if (!regExp.test(user.userid)) {
            alert('입력이 올바르지 않습니다.')
        } else {
            const data = user;

            axios.post('/api/login', data).then(res => {

                if (res.data.code === 200) {
                    dispatch(actions.userLogin(res.data.userid));
                    if (check) {
                        setCookie("accessToken", res.data.accessToken, {
                            path: '/',
                            sameSite: "Lax",
                            maxAge: new Date(Date.now() + 1800)
                        });

                        setCookie("refreshToken", res.data.refreshToken, {
                            path: '/',
                            sameSite: "Lax",
                            maxAge: new Date(Date.now() + (60 * 60 * 24 * 10))
                        });

                        setCookie("logged", res.data.userid, {
                            path: '/',
                            sameSite: "Lax",
                        });

                    } else {
                        setCookie("accessToken", res.data.accessToken, {
                            path: '/',
                            sameSite: "Lax",
                            maxAge: new Date(Date.now() + 1800)
                        });

                        setCookie("refreshToken", res.data.refreshToken, {
                            path: '/',
                            sameSite: "Lax",
                            maxAge: new Date(Date.now() + (60 * 60 * 24 * 30))
                        });

                        setCookie("logged", res.data.userid, {
                            path: '/',
                            sameSite: "Lax",
                            maxAge: new Date(Date.now() + (60 * 60 * 24 * 30))
                        });

                        setCookie("remember", 0, {
                            path: '/',
                            sameSite: "Lax",
                            maxAge: new Date(Date.now() + (60 * 60 * 24 * 30))
                        })
                    }

                    histoty.push('/')

                } else {
                    window.alert(res.data.message)
                }


            })
        }
    }




    const auth = (e) => {
        e.preventDefault();
        axios.get('/sample/admin').then(res => {
            console.log(res)
        })
    }


    const handleInput = (e) => {
        e.persist();
        setUser({ ...user, [e.target.name]: e.target.value })
    };

    const handleCheck = (e) => {
        e.persist();
        setCheck(!check);
    }



    if (loading) {
        return (
            <></>
        )
    } else {
        return (
            <main className="form-signin">
                <form onSubmit={loginSubmit}>
                    <h1 className="h3 mb-3 mt-4 fw-normal">Login</h1>
                    <Link to='/user/sign-up'>아이디가 없으신가요?</Link>

                    <div className="form-floating mt-4">
                        <input type="text" className="form-control" name="userid" onChange={handleInput} value={user.userid} id="floatingInput" placeholder="ID" />
                        <label>ID</label>
                    </div>
                    <div className="form-floating mt-1">
                        <input type="password" className="form-control" name="password" onChange={handleInput} value={user.password} autoComplete="on" id="floatingPassword" placeholder="Password" />
                        <label>Password</label>
                    </div>
                    {/* <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> */}
                    <div className="form-check">
                        <input className="form-check-input" name="autoLogin" type="checkbox" value={check} onChange={handleCheck} id="flexCheckDefault" />
                        <label className="form-check-label" htmlFor="flexCheckDefault">
                            로그인유지 (30일)
                        </label>
                    </div>

                    <button className="w-100 btn btn-lg btn-success mt-1" type="submit">Login</button>

                </form>

                {/* <button className="w-100 btn btn-lg btn-success mt-1" type="button" onClick={auth}>auth</button> */}
            </main>
        )
    }
}

export default UserSignIn;