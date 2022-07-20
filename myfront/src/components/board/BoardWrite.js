import React, { useState } from "react";
import axios from 'axios';
import { useHistory } from "react-router-dom";
import { useSelector, useDispatch } from 'react-redux';
import { useCookies } from 'react-cookie';
import actions from '../../redux/reducers/users/users.action';

function BoardWrite() {

    const { userid } = useSelector(state => state.users);
    const [cookies, setCookie, removeCookie] = useCookies();
    const history = useHistory();
    const dispatch = useDispatch();
    const [imgPreview, setImgPreview] = useState();
    const [img, setImg] = useState();
    const [error, setError] = useState(false);


    const [postValue, setPostValue] = useState({
        title: "",
        content: "",
        writer: userid,
    })





    const handleInput = (e) => {
        e.persist();
        setPostValue({ ...postValue, [e.target.name]: e.target.value })
    }


    const newPost = (e) => {
        e.preventDefault();

        if(error) {
            alert('업로드 파일 유형이 올바르지 않습니다.');
            return;
        }

        const formData = new FormData();

        formData.append('title', postValue.title);
        formData.append('content', postValue.content);
        formData.append('writer', postValue.writer);
        if(img) {
            formData.append('img', img)
        }

        // const data = {
        //     title: postValue.title,
        //     content: postValue.content,
        //     writer: postValue.writer,
        //     img: img,
        // };

        axios.post(`/board/register`, formData).then(res => {

            let jwtException = res.data.exceptionName;

            if (jwtException != null && jwtException !== "Expired") {

                removeCookie('accessToken', { path: '/' });
                removeCookie('refreshToken', { path: '/' });
                removeCookie('logged', { path: '/' });
                removeCookie('remember', { path: '/' });

                dispatch(actions.userLogout());
                alert('인증정보가 정확하지 않습니다. 다시 로그인해주세요.')
                history.push('/user/sign-in')
                history.go(0)

            } else if (jwtException === 'Expired') {

                axios.get(`/api/token/refresh`, {
                    headers: {
                        "Authorization": `Bearer ${cookies.refreshToken}`
                    }

                }).then(res => {

                    if (res.data.code === 403 || res.data.exceptionName === "RefreshExpired") {

                        // 리프레쉬 토큰도 만료된 상황
                        removeCookie('accessToken', { path: '/' });
                        removeCookie('refreshToken', { path: '/' });
                        removeCookie('logged', { path: '/' });
                        removeCookie('remember', { path: '/' });

                        dispatch(actions.userLogout());
                        alert('세션이 만료되었습니다. 다시 로그인해주세요.')
                        history.push('/user/sign-in')
                        history.go(0)

                    } else if (res.data.success) {

                        setCookie("accessToken", res.data.accessToken, {
                            path: '/',
                            sameSite: "Lax",
                            maxAge: new Date(Date.now() + 1800)
                        });

                        axios.post(`/board/register`, formData).then(res => {
                            if (res.data.code === 200 && res.data.success) {
                                alert(res.data.message);
                                history.push("/board/list?page=1")
                                // history.go(0)
                            } else {
                                alert("등록에 실패하였습니다." + res.data.message);
                            }
                        })
                    }
                })
            } else {
                if (res.data.code === 200 && res.data.success) {
                    alert(res.data.message);
                    history.push("/board/list?page=1")
                    // history.go(0)
                } else {
                    alert("등록에 실패하였습니다." + res.data.message);
                }
            }






        })
    }

    const handleImageChange = (e) => {
        setImgPreview("");
        const selected = e.target.files[0];
        const ALLOWED_TYPES = ["image/png", "image/jpeg", "image/jpg"];

        if (selected && ALLOWED_TYPES.includes(selected.type)) {
            // console.log("selected");
            setError(false);
            // let reader = new FileReader();
            // reader.onloadend = () => {
            //     setImgPreview(selected);
            // }
            // reader.readAsDataURL(selected);
            let url = URL.createObjectURL(selected);
            console.log(url)
            setImg(e.target.files[0]);
            
            setImgPreview(url)
        } else {
            setError(true);
            console.log("file not supported");
        }
        console.log(img)
    }

    const deleteImage = (e) => {
        URL.revokeObjectURL(imgPreview);
        setImgPreview(null);
        setImg(null);
    };

    return (
        <div className="row">
            <div className="col-lg-12">
                <h1 className="board-title mt-4">New Post </h1>

                <form onSubmit={newPost} id="newPost">
                    <div className="input-group mt-3">
                        <div className="input-group mb-3">
                            <input type="text" className="form-control" onChange={handleInput} value={postValue.title} name="title" placeholder="TITLE (Max Bytes 200)" required maxLength={60} />
                        </div>
                        <div className="input-group mb-3">
                            <textarea type="text" className="form-control post-content" onChange={handleInput} value={postValue.content} name="content" placeholder="CONTENT (Max Bytes 2000)" maxLength={650} required />
                        </div>
                        <div className="input-group mb-3">
                            {
                                // 나중에 writer value 에 user id 얻어서 입력
                            }
                            <textarea type="text" className="form-control post-content" name="writer" style={{ display: 'none' }} />
                        </div>
                        <div className="input-group mb-3">
                        <label htmlFor="fileUpload" className="customFileUpload">Only jpg, jpeg, png</label>    
                        </div>

                        
                        
                        
                        <div className="imgPreview">
                                    
                                    <input type="file" id="fileUpload" onChange={handleImageChange}/>
                                    {imgPreview ? <button onClick={() => deleteImage()}>삭제</button> : null}
                                    {error && <p className="errorMsg">File not supported</p>}
                            
                        </div>
                        {
                            imgPreview
                            ?
                            <>
                            <img alt="img" src={imgPreview} style={{height:"150px", width:"150px" }}></img>
                            
                            </>
                            : null

                        }
                        
                    </div>
                    


                    <div className="btn-list mt-4">
                        <button type="button" form="newPost" onClick={() => { history.goBack(); }} className="btn btn-primary btn-sm" >List</button>
                        <button type="submit" className="btn btn-success btn-sm" >submit</button>
                    </div>
                </form>
            </div>
        </div>
    )
}

export default BoardWrite;