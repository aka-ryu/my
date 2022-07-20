import axios from "axios";
import React, { useEffect, useState } from "react";
import '../../css/board.css';
import { useHistory } from "react-router-dom";
import { useSelector, useDispatch } from 'react-redux';
import { useCookies } from 'react-cookie';
import actions from '../../redux/reducers/users/users.action';


function BoardDetail() {
    const userid = useSelector(state => state.users);
    const [auth, setAuth] = useState(null);
    const [board, setBoard] = useState({});
    const bno = window.location.search.substring(5);
    const history = useHistory();
    const [loading, setLoading] = useState(true);
    const [getDetail, setGetDetail] = useState(false);
    const [cookies, setCookie, removeCookie] = useCookies();
    const dispatch = useDispatch();
    const [boardImgPreview, setboardImgPreview] = useState();
    const [imgPreview, setImgPreview] = useState();
    const [error, setError] = useState(false);
    const [img, setImg] = useState();
    const [deleteImg, setDeleteImg] = useState(false);



    const LoginCheck = () => {
        if (cookies.accessToken && cookies.logged && cookies.refreshToken) {


            setAuth(userid);
            setGetDetail(true);



            axios.get(`/board/detail?bno=${bno}`).then(res => {


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

                            axios.get(`/board/detail?bno=${bno}`).then(res => {
                                if (!(res.data.writer === cookies.logged)) {
                                    alert("비정상적인 접근 입니다. 게시글 수정은 본인의 글만 가능합니다. (작성자가 아님)");
                                    history.push('/');
                                    history.go(0);
                                } else {
                                    setBoard(res.data);
                                    setLoading(false);
                                    if (res.data.imgURL) {
                                        setboardImgPreview(res.data.imgURL)
                                    }
                                }
                            })
                        }
                    })
                } else {
                    if (!(res.data.writer === cookies.logged)) {
                        alert("비정상적인 접근 입니다. 게시글 수정은 본인의 글만 가능합니다. (작성자가 아님)");
                        history.push('/');
                        history.go(0);
                    } else {
                        setBoard(res.data);
                        setLoading(false);
                        if (res.data.imgURL) {
                            setboardImgPreview(res.data.imgURL)
                        }
                    }
                }
            })




        } else {
            alert("비정상적인 접근 입니다. 게시글 수정은 본인의 글만 가능합니다. (로그인 정보 없음)");
            history.push('/user/sign-in');
            history.go(0);
        }
    }


    useEffect(() => {
        LoginCheck();

    }, [])

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



    const removeOriginImg = () => {
        setboardImgPreview('');
        setDeleteImg(true);
    }

    const handleInput = (e) => {
        e.persist();
        setBoard({ ...board, [e.target.name]: e.target.value })
        console.log(board);
    }

    const goBackHandle = () => {
        history.goBack();
    }

    const editHandle = async (e) => {
        e.preventDefault();
        // const data = board;
        const formData = new FormData();

        formData.append('title', board.title);
        formData.append('content', board.content);
        formData.append('writer', board.writer);
        formData.append('id', bno)
        if(img) {
            formData.append('img', img)
        }
        if(deleteImg) {
            formData.append('imgDelete', true);
        }
        
        await axios.put(`/board/modify`, formData).then(res => {
            console.log(res);
            if (res.data.success && res.data.code === 200) {
                alert(res.data.message);
                history.push(`/board/detail?bno=${bno}`);
            }
        })
    }

    if (loading) {
        return (
            <></>
        )
    } else {



        return (
            <div className="row">
                <form>
                    <div className="col-lg-12">
                        <h1 className="board-title mt-4"># Edit {board.id || ''} </h1>

                        <div className="input-group mt-3">
                            <div>
                                <p><strong>Writer :</strong> {board.writer || ''}</p>
                                <p><strong>RegDate :</strong> {board.regdate || ''}</p>
                            </div>
                            <div className="input-group mb-3">
                                <input id="title" name="title" className="form-control" value={board.title || ''} onChange={handleInput} />
                            </div>
                            <div className="input-group mb-3">
                                <textarea className="form-control post-content" name="content" value={board.content || ''} onChange={handleInput} id="content" />
                            </div>

                        </div>
                        {
                            boardImgPreview
                                ?
                                <>
                                    <h4 className="mt-5">현재 첨부 이미지</h4>
                                    <img style={{ width: "200px", height: "200px" }} src={boardImgPreview} /><br></br>
                                    <button className="btn btn-danger mt-1" onClick={removeOriginImg}>기존이미지 삭제</button>
                                </>
                                :
                                null
                        }

                        {
                            boardImgPreview
                            ?
                            <h4 className="mt-5">첨부 이미지 변경하기</h4>
                            :
                            <h4 className="mt-5">이미지 첨부</h4>
                        }
                        <div className="imgPreview">

                            <input type="file" id="fileUpload" onChange={handleImageChange} />
                            {imgPreview ? <button onClick={() => deleteImage()}>삭제</button> : null}
                            {error && <p className="errorMsg">File not supported</p>}

                        </div>
                        
                        
                        {
                            imgPreview
                                ?
                                <>
                                    <img alt="img" src={imgPreview} style={{ height: "200px", width: "200px" }}></img>

                                </>
                                : null

                        }

                        <div className="btn-list mt-5">
                            <button type="button" value="back" className="btn btn-warning btn-sm" onClick={goBackHandle}>Back</button>
                            <button type="submit" className="btn btn-success btn-sm" onClick={editHandle}>Edit</button>
                        </div>
                    </div>
                </form>
            </div>
        )

    }
}

export default BoardDetail;