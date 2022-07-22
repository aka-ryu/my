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

    const [emailCode, setEmailCode] = useState({
        emailCode : ''
    });

    const [passwordCheck, setPasswordCheck] = useState("");
    const [passwordLabel, setPasswordLabel] = useState("");
    const { userid } = useSelector(state => state.users);
    const [loading, setLoading] = useState(true);
    const [cookies] = useCookies();
    const [emailCk, setEmailCk] = useState(false);
    const [emailOverlap, setEmailOverlap] = useState(false);
    const [emailLoading, setEmailLoading] = useState(false);



    const history = useHistory();

    const JoinSubmit = (e) => {
        
        e.preventDefault();
        if(!emailCk) {
            alert('이메일 인증을 완료 후 가입할수 있습니다.')
        } else {
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

    const emailCheckHandle = async () => {

        let regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
        if(!regExp.test(user.email)){
            alert('이메일을 제대로 입력해주세요')
            return;
        }

        let data = {
            email: user.email
        };

        let emailCheck = await axios.post(`/email/code`, data);

        if(emailCheck.data.code === 400) {
            alert(emailCheck.data.message);
        } else if(emailCheck.data.code === 200) {
            alert(emailCheck.data.message);
            setEmailOverlap(true);
        }
        

        
        // setEmailOverlap(true);
    }



    const handleInput = (e) => {
        e.persist();

        if (e.target.name === "passwordck") {
            setPasswordCheck(e.target.value);
        } else {
            setUser({ ...user, [e.target.name]: e.target.value });
        }
    };

    const emailCodeInput = (e) => {
        e.persist();
        setEmailCode({...emailCode, [e.target.name] : e.target.value})
    }

    const certificationHandle = async () => {

        let data = {
            email: user.email,
            code: emailCode.emailCode
        };

        let emailCertification = await axios.post(`/email/certification`, data);

        if(emailCertification.data.code === 400) {
            alert('인증번호가 올바르지 않습니다.');
        } else if (emailCertification.data.code === 200) {
            setEmailCk(true);
            alert('이메일 인증이 완료되었습니다.');
        }



    }

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
                    <div className="form-floating mt-2 input-group">
                        <input type="email" className="form-control" name="email" onChange={handleInput} value={user.email} placeholder="name" />
                        <label>Email</label>
                        {
                            !emailOverlap
                            ?
                            <button className="btn btn-success" type="button" onClick={emailCheckHandle}>이메일 인증</button>
                            : null
                        }
                    </div>

                    {
                        emailOverlap
                        ?
                            <div className="form-floating mt-2 input-group">
                                <input type="text" className="form-control" name="emailCode" onChange={emailCodeInput} placeholder="name" />
                                <label>Email Code</label>
                                <button className="btn btn-success" type="button" onClick={certificationHandle}>인증번호 확인</button>
                            </div>

                        : <p>
                            이메일로 인증코드 를 보내는 작업은 최대 10초까지 소요됩니다. <br/>
                            버튼을 누르고 잠시만 기다려주세요.
                        </p>
                        
                    }
                    
                    <label>{passwordLabel}</label>
                    <button className="w-100 btn btn-lg btn-primary mt-2" type="submit">Join</button>
                </form>
            </main>
        )
    }
}

export default UserSignUp;