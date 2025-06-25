import React, {useState} from 'react';
import './index.css';
import {Col, Row} from "antd";

const Authorization = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState(null);

    const handleSubmit = async (event) => {
        event.preventDefault();

        console.log(`Отправлено email: ${email}, пароль: ${password}`);

        if (email === '' || password === '') {
            setErrorMessage('Все поля обязательны');
        } else {
            setErrorMessage(null);
            alert('Форма успешно отправлена!');
        }
    };

    return (
        <div className="auth-container">
            <Row justify="center">
                <Col span={12} offset={2}>
                    <h1>Добро пожаловать в сервис суммаризации презентаций!</h1>
                </Col>
            </Row>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <Row justify="center">
                <Col span={12} offset={8}>
                    <form onSubmit={handleSubmit} className="login-form">
                        <h3>Авторизация</h3>
                        {errorMessage && <div className="alert-error">{errorMessage}</div>}
                        <label htmlFor="email">Email:</label>
                        <input
                            type="email"
                            id="email"
                            placeholder="Введите email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                        <br/>
                        <label htmlFor="password">Пароль:</label>
                        <input
                            type="password"
                            id="password"
                            placeholder="Введите пароль"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                        <button type="submit">Войти</button>
                    </form>
                </Col>
            </Row>
        </div>
    );
};

export default Authorization;