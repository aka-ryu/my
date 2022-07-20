import axios from "axios";
import React, { useEffect, useState } from "react";
import '../../css/board.css';
import { useHistory } from "react-router-dom";
import { useSelector, useDispatch } from 'react-redux';
import actions from '../../redux/reducers/users/users.action';
import { useCookies } from 'react-cookie';

function BoardDetail(props) {
    const bno = window.location.search.substring(5);
    const [board, setBoard] = useState({});
    const [reply, setReply] = useState("");
    const [replyList, setReplyList] = useState();
    const [writer, setWriter] = useState(false);
    const [loading, setLoading] = useState(true);
    const [render, setRender] = useState(false);
    const [isChecked, setIsChecked] = useState(false);
    const { userid, rememberme } = useSelector(state => state.users);
    const dispatch = useDispatch();
    const history = useHistory();
    const [cookies, setCookie, removeCookie] = useCookies();
    const [imgPreview, setImgPreview] = useState();


    useEffect(() => {

        if (cookies.accessToken && cookies.logged && cookies.refreshToken) {
            axios.get(`/board/detail?bno=${bno}`).then(res => {

                console.log(res)
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

                                if (jwtException == null) {

                                    setLoading(false);
                                    if(res.data.imgURL) {
                                        setImgPreview(res.data.imgURL)
                                    }
                                    setBoard(res.data)

                                }

                                if (userid === res.data.writer) {
                                    setWriter(true);
                                }
                                history.go(0)
                            })
                        }
                    })
                } else {
                    if (jwtException == null) {

                        setLoading(false);
                        setBoard(res.data)
                        if(res.data.imgURL) {
                            setImgPreview(res.data.imgURL)
                        }
                    }

                    if (userid === res.data.writer) {
                        setWriter(true);
                    }
                }
            })
        } else {
            alert('인증정보가 정확하지 않습니다.')
        }
        getReplyList();
        console.log(replyList)
    }, [loading]);



    const getReplyList = async () => {
        let getReplyList = await axios.get(`/reply/list?bno=${bno}`)
        setReplyList(getReplyList.data.replyDTOList);
    }

    const replyValue = (e) => {
        e.persist();
        setReply(e.target.value);
        console.log(reply)
    }

    const replyRegister = async() => {
        const data = {
            boardId : bno,
            replyContent : reply,
            replyWriter : userid
        }
        let replyRegister = await axios.post(`/reply/register`,data);
        console.log(replyRegister)
        if(replyRegister.data.code === 200) {
            alert("댓글이 등록되었습니다.");
            history.go(0);
        }
    }

    const deleteReply = async(e) => {
        let rno = e.target.value;
        let replyDelete = await axios.delete(`/reply/delete?rno=${rno}`)
        console.log(replyDelete)
        if(replyDelete.data.code === 200) {
            alert("댓글이 삭제되었습니다.");
            history.go(0);
        }
    }


    const goBackHandler = () => {

        history.goBack();
    }

    const modifyHandler = () => {
        history.push(`/board/edit?bno=${bno}`);
    }

    const removeHandler = async () => {
        await axios.delete(`/board/delete?bno=${bno}`).then(res => {
            
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

                        axios.delete(`/board/delete?bno=${bno}`).then(res => {

                            if (res.data.code === 200 && res.data.success) {
                                alert(res.data.message)
                                history.goBack();
                            } else {
                                alert("처리중 사용자 인증관련 문제가 발생하였습니다.")
                            }
                        })
                    }
                })

            } else {

                if (res.data.code === 200 && res.data.success) {
                    alert(res.data.message)
                    history.goBack();
                } else {
                    alert("처리중 사용자 인증관련 문제가 발생하였습니다.")
                }
            }
        });
    }



    // useEffect(() => {

    // }, [])


    if (loading) {

        return (
            <></>
        )

    } else {

        return (
            <div className="row">
                <div className="col-lg-12">
                    <h1 className="board-title mt-4"># Detail {board.id}</h1>

                    <div className="input-group mt-3">
                        <div>
                            <p><strong>Writer :</strong> {board.writer}</p>
                            <p><strong>RegDate :</strong> {board.regdate}</p>
                        </div>
                        <div className="input-group mb-3">
                            <input type="text" className="form-control" value={board.title || ""} readOnly />
                        </div>
                        <div className="input-group mb-3">
                            <textarea type="text" className="form-control post-content" value={board.content || ""} readOnly />
                        </div>
                    </div>

                    <div className="btn-list">
                        <button type="button" value="back" className="btn btn-warning btn-sm" onClick={goBackHandler}>Back</button>
                        {
                            writer
                                ?
                                <>
                                    <button type="button" value="edit" className="btn btn-secondary btn-sm" onClick={modifyHandler}>Modify</button>
                                    <button className="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#exampleModal">remove</button>
                                </>
                                : null
                        }

                    </div>
                </div>

                {
                    imgPreview 
                    ?
                    <>
                        <h4 className="mt-5">첨부 이미지</h4> 
                        <img style={{width:"200px", height:"200px"}} src={imgPreview}/>
                        <a href={imgPreview}>다운로드</a>
                    </> 
                    : 
                    null
                }

                <h3 className="board-title mt-4">Reply Register</h3>
                <div className="input-group mb-3 mt-2">
                            <input type="text" onChange={replyValue} className="form-control" />
                            <button className="btn btn-success"  onClick={replyRegister}>등록</button>
                </div>

                <h3 className="board-title mt-4 mb-5">Reply List</h3>
                
                {
                    replyList && replyList.map((reply) => {
                        return(
                            <div>
                                <p><strong>{reply.replyWriter}</strong> <span>{reply.regdate}</span> {reply.replyWriter === userid ? <button value={reply.id} onClick={deleteReply}>삭제</button> : null}</p>
                                <p>{reply.replyContent}</p>
                            </div>
                        )
                    })
                }
                

                <div className="modal fade" id="exampleModal" tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title" id="exampleModalLabel">경고 !</h5>
                                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div className="modal-body">
                                삭제한 게시물은 다시 복구할수 없습니다.<br />
                                정말 삭제하시겠습니까?
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                                <button type="submit" onClick={removeHandler} className="btn btn-danger"
                                    data-bs-dismiss="modal">삭제</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default BoardDetail;