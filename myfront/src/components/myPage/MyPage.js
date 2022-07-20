import react from 'react';
import { Link } from 'react-router-dom/cjs/react-router-dom';

const MyPage = () => {
    return (
        <div className='container'>
            <h2 className='mypage-title mt-4'># Mypage</h2>
            <div className='item mt-4'>
                <nav className="navbar">
                    <div className="content">
                        <Link to="#" className="board">Profile</Link>
                        <Link to="#" className="token">MyItem</Link>
                        <Link to="#" className="admin">admin</Link>
                    </div>
                </nav>
            </div>
        </div>
    )
}

export default MyPage;