import React, { useState, useEffect } from "react";
import '../../css/board.css';
import axios from "axios";
import { Link } from "react-router-dom/cjs/react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import actions from '../../redux/reducers/users/users.action';
import { useHistory } from 'react-router-dom';
import { useCookies } from 'react-cookie';

function BoardList() {

    const [postList, setPostList] = useState([]);
    const [pageList, setPageList] = useState([]);
    const [pageNum, setPageNum] = useState(window.location.search.substring(6));
    const [prev, setPrev] = useState(false);
    const [next, setNext] = useState(false);
    const history = useHistory();
    // const [url, setUrl] = useState(pageNum);
    let pN = window.location.search.substring(6);
    const [cookies, setCookie, removeCookie] = useCookies();
    const [loading, setLoading] = useState(true);


    const { userid } = useSelector(state => state.users);

    // if(parseInt(pageNum) !== parseInt(pN)) {
    //     setPageNum(pN);
    // }    

    useEffect(() => {
        boardFn();
    }, [pageNum]);

    if (pageNum === "") {
        setPageNum(1);
    }



    const boardFn = async () => {

        await axios.get(`board/list?page=${pageNum}`).then(res => {

            console.log(res)
            setPostList(res.data.dtoList);
            setPageList(res.data.pageList);


            if (res.data.prev === true) {
                setPrev(true);
            } else {
                setPrev(false);
            };

            if (res.data.next === true) {
                setNext(true);
            } else {
                setNext(false);
            };

            setLoading(false)
        })
    }


    const DetailHandle = (e) => {
        if (!userid || !cookies.logged || !cookies.accessToken || !cookies.refreshToken) {
            alert('상세내용은 회원만 조회 가능합니다.')
        } else {
            history.push(`/board/detail?bno=${e.target.value}`)
        }
    }

    const prevHandler = (e) => {
        e.persist();
        setPageNum(parseInt(pageNum) - 1);
    }

    const nextHandler = (e) => {
        e.persist();
        setPageNum(parseInt(pageNum) + 1);
        console.log(pageNum);
    }


    const WriteHandle = () => {
        if (cookies.logged && cookies.refreshToken && cookies.accessToken) {
            history.push(`/board/write`)
        } else {
            alert('게시글 작성은 회원만 가능합니다.')
        }
    }

    if (loading) {
        return (
            <></>
        )
    } else {

        return (
            <div className="row">
                <div className="col-lg-12">
                    <h2 className="board-title mt-4">#Board {pageNum} Page</h2>
                    <table className="table table-striped table-bordered table-hover">
                        <thead>
                            <tr>
                                <th className="col-1">#번호</th>
                                <th className="col-6">제목</th>
                                <th className="col-1">작성자</th>
                                <th className="col-1">작성일</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                postList && postList.map((item) => {
                                    return (
                                        <tr key={item.id}>
                                            <td>{item.id}</td>
                                            <td>
                                                <button className="post-title" value={item.id} onClick={DetailHandle} >{item.title} </button>
                                            </td>
                                            <td>{item.writer}</td>
                                            <td>{item.regdate}</td>
                                        </tr>
                                    )
                                })
                            }
                        </tbody>
                    </table>
                    <div className="board-footer d-flex">
                        <ul className="pagination">
                            {
                                prev
                                    ? <li className="page-item" onClick={prevHandler}><Link className="page-link" to={`/board/list?page=${parseInt(pageNum - 1)}`}>Previous</Link></li>
                                    : null
                            }

                            {
                                pageList && pageList.map((item) => {
                                    let active;

                                    if (parseInt(item) === parseInt(pageNum)) {
                                        active = "active";
                                    } else {
                                        active = null;
                                    }


                                    return (
                                        <li key={item} className={`page-item ${active}`} onClick={() => { setPageNum(item) }}><Link className="page-link" value={`${item}`} to={`/board/list?page=${item}`}>{item}</Link></li>
                                    )
                                })
                            }

                            {
                                next
                                    ? <li className="page-item" onClick={nextHandler}><Link className="page-link" to={`/board/list?page=${parseInt(pageNum) + 1}`}>Next</Link></li>
                                    : null
                            }



                        </ul>

                        <div className="board-serach">
                            <button className="btn btn-success" onClick={WriteHandle}>Write</button>
                        </div>


                    </div>
                </div>
            </div>
        )
    }
}

export default BoardList;