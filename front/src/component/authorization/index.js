import {useState} from "react";
import {useAuth} from "../authorizationProvider/index";
import './index.css';
import {Button, Col, Form, Input, Row} from "antd";

const Authorization = () => {
    const [input, setInput] = useState({
        login: "",
        password: "",
    });

    const auth = useAuth();
    const handleSubmitEvent = (e) => {
        e.preventDefault();
        if (input.login !== "" && input.password !== "") {
            auth.loginAction(input).then(r => "");
            return;
        }
        alert("Пожалуйста, укажите корректные данные");
    };

    const handleInput = (e) => {
        const {name, value} = e.target;
        setInput((prev) => ({
            ...prev,
            [name]: value,
        }));
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
                    <form className="login-form" onSubmit={handleSubmitEvent}>
                        <Form.Item>
                            <label htmlFor="login">Логин:</label>
                            <Input
                                type="login"
                                id="user-login"
                                name="login"
                                placeholder="Введите логин"
                                aria-describedby="user-login"
                                aria-invalid="false"
                                onChange={handleInput}
                            />
                        </Form.Item>
                        <Form.Item>
                            <label htmlFor="password">Пароль:</label>
                            <Input.Password
                                type="password"
                                id="password"
                                name="password"
                                placeholder="Введите пароль"
                                aria-describedby="user-password"
                                aria-invalid="false"
                                onChange={handleInput}
                            />
                        </Form.Item>
                        <Button type="primary" htmlType="submit">Войти</Button>
                    </form>
                </Col>
            </Row>
        </div>
    );
};

export default Authorization;