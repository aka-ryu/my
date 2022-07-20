import React, { useState } from "react";
import axios from "axios";
import { useHistory } from "react-router-dom";
import { useSelector } from 'react-redux';
import { useEffect } from "react";
import { Link } from 'react-router-dom/cjs/react-router-dom';
import { useCookies } from 'react-cookie';

function UserSignUp() {

    const [user, setUser] = useState({
        userid: '',
        password: '',
        nickname: '',
        email: '',
    });

    const [passwordCheck, setPasswordCheck] = useState("");
    const [passwordLabel, setPasswordLabel] = useState("");
    const { userid } = useSelector(state => state.users);
    const [loading, setLoading] = useState(true);
    const [cookies] = useCookies();



    const history = useHistory();

    const JoinSubmit = (e) => {
        e.preventDefault();
        if (user.password !== passwordCheck) {
            alert("비밀번호의 값이 서로 다릅니다.")
        
        } else if( !user.userid || !user.password || !user.nickname || !user.email) {
            alert('모든 정보를 입력해주세요')
        } else {
            const data = user;
            axios.post('/api/register', data).then(res => {
                if (res.data.code === 400) {
                    alert(res.data.message)
                } else if (res.data.code === 200) {
                    alert(res.data.message);
                    history.push('/user/sign-in');
                }
            })
        }
    }

    const userCheck = () => {
        if(cookies.logged != null) {
            setLoading(true);
        } else {
            setLoading(false)
        }
    }

    useEffect(() => {
        userCheck();
    },[])



    const handleInput = (e) => {
        e.persist();

        if (e.target.name === "passwordck") {
            setPasswordCheck(e.target.value);
        } else {
            setUser({ ...user, [e.target.name]: e.target.value });
        }

    };

    if (loading) {
        return (
            <></>
        )
    } else {


        return (
            <main className="form-signin">
                <form onSubmit={JoinSubmit}>
                    <h1 className="h3 mb-3 mt-4 fw-normal">회원가입</h1>
                    <Link to='/user/sign-in'>아이디가 있으신가요?</Link>

                    <div className="form-floating mt-4">
                        <input type="text" className="form-control" name="userid" onChange={handleInput} value={user.userid} id="floatingInput" placeholder="ID" minLength={4} maxLength={8} />
                        <label>ID : 4~8글자</label>
                    </div>

                    <div className="form-floating mt-2">
                        <input type="password" className="form-control" name="password" autoComplete="off" onChange={handleInput} value={user.password} id="floatingPassword" minLength={8} placeholder="Password" />
                        <label>Password : 최소8글자 특문,영어 필수</label>
                    </div>
                    <div className="form-floating mt-2">
                        <input type="password" className="form-control" name="passwordck" autoComplete="off" onChange={handleInput} placeholder="Password ck" minLength={8} />
                        <label>Password Check</label>
                    </div>
                    <div className="form-floating mt-2">
                        <input type="text" className="form-control" name="nickname" onChange={handleInput} value={user.nickname} minLength={2} maxLength={6} placeholder="name" />
                        <label>NickName : 2~6글자</label>
                    </div>
                    <div className="form-floating mt-2">
                        <input type="email" className="form-control" name="email" onChange={handleInput} value={user.email} placeholder="name" />
                        <label>Email</label>
                    </div>
                    <label>{passwordLabel}</label>
                    <button className="w-100 btn btn-lg btn-primary mt-2" type="submit">Join</button>
                </form>
            </main>
        )
    }
}

export default UserSignUp;