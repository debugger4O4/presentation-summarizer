import './index.css';
import React from 'react';
import {Button, Checkbox, Row, Form, Input, Col, Card} from 'antd';

const onFinish = values => {
    console.log('Success:', values);
};
const onFinishFailed = errorInfo => {
    console.log('Failed:', errorInfo);
};

const AuthorizationComponent = () => {

    return (
        <div className="main">
            <Row>
                <Col span={12} type="flex" align="middle">
                    <Card style={{ width: "40%" }} >
                        <Form
                            name="basic"
                            labelCol={{span: 8}}
                            wrapperCol={{span: 16}}
                            style={{maxWidth: 600}}
                            initialValues={{remember: true}}
                            onFinish={onFinish}
                            onFinishFailed={onFinishFailed}
                            autoComplete="off"
                            component="true"
                        >
                            <Form.Item
                                label={<label style={{color: "black"}}>Логин</label>}
                                name="username"
                                rules={[{required: true, message: 'Введите логин!'}]}
                            >
                                <Input/>
                            </Form.Item>

                            <Form.Item
                                label={<label style={{color: "black"}}>Пароль</label>}
                                name="password"
                                rules={[{required: true, message: 'Введите пароль!'}]}
                            >
                                <Input.Password/>
                            </Form.Item>

                            <Form.Item name="remember" valuePropName="checked" label={null}>
                                <Checkbox style={{color: "black"}}>Запомнить</Checkbox>
                            </Form.Item>

                            <Form.Item label={null}>
                                <Button type="primary" htmlType="submit">
                                    Вход
                                </Button>
                            </Form.Item>
                        </Form>
                    </Card>
                </Col>
            </Row>
        </div>
    )
}


export default AuthorizationComponent;