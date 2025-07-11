import React, {useState} from 'react';
import {useAuth} from "../../component/authorizationProvider";
import pdfToText from 'react-pdftotext';
import './index.css';
import {InboxOutlined} from '@ant-design/icons';
import {Button, Form, Select, Upload} from 'antd';
import axios from 'axios';

const PdfDragAndDropUploader = () => {

    const [slidesCount, setSlidesCount] = useState(0);
    const [textForSummarize, setTextForSummarize] = useState('');
    const [downloading, setDownloading] = useState(false);
    const auth = useAuth();

    const handleFileChange = async ({fileList}) => {
        if (downloading) {
            return;
        }
        setDownloading(true);
        const promises = fileList.map(file => {
            if (!file.originFileObj) {
                return null;
            }
            switch (file.type) {
                case 'application/pdf':
                    return pdfToText(file.originFileObj);
                case 'application/vnd.openxmlformats-officedocument.wordprocessingml.document':
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        return e.target.result;
                    };
                    return '';
                case 'text/plain':
                    return new Promise(async resolve => resolve(await file.originFileObj.text()));
                default:
                    return null;
            }
        });
        const texts = await Promise.allSettled(promises.filter(Boolean));
        let content = '';
        texts.forEach(result => {
            if (result.status === 'fulfilled') {
                content += result.value + ' ';
            }
        });
        setDownloading(false);
        setTextForSummarize(content);
    };

    const downloadPresentation = async () => {
        setDownloading(true);
        try {
            const response = await axios({
                url: 'http://localhost:8080/summarize/getSummarize',
                method: 'POST',
                data: {
                    slidesCount,
                    textForSummarize
                },
                responseType: 'blob', // Важно указать blob для бинарных данных.
            });

            // Создаем ссылку для скачивания файла.
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'summarize.pptx');
            document.body.appendChild(link);
            link.click();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error('Error loading the presentation:', error);
        } finally {
            setDownloading(false);
        }
    }

    const normFile = e => {
        console.log('Upload event:', e);
        if (Array.isArray(e)) {
            return e;
        }
        return e && e.fileList;
    };

    const options = [];
    for (let i = 1; i <= 20; i++) {
        const value = i;
        options.push({
            label: value,
            value
        });
    }

    const handleChange = value => {
       setSlidesCount(value);
    };

    return (
        <div>
            <div className="upload-center-container">
                <Form>
                    <Form.Item>
                        <Form.Item name="dragger" valuePropName="fileList" getValueFromEvent={normFile} noStyle>
                            <Upload.Dragger name="files" onChange={handleFileChange} multiple>
                                <p className="ant-upload-drag-icon"><InboxOutlined/></p>
                                <p className="ant-upload-text">Щелкните или перетащите файл в эту область для
                                    загрузки</p>
                                <p className="ant-upload-hint">Поддержка загрузки одного или нескольких файлов.</p>
                            </Upload.Dragger>
                        </Form.Item>
                        <br/>
                        <br/>
                        <br/>
                        <Form.Item style={{display: 'flex', justifyContent: 'center'}}>
                            <Select
                                allowClear
                                options={options}
                                placeholder="Количество слайдов"
                                onChange={handleChange}
                            />
                        </Form.Item>
                        <Form.Item style={{display: 'flex', justifyContent: 'center'}}>
                            <Button type={"primary"} disabled={downloading} onClick={downloadPresentation}>
                                Суммаризировать презентации
                            </Button>
                        </Form.Item>
                        <Form.Item style={{display: 'flex', justifyContent: 'center'}}>
                            <Button type={"primary"} danger onClick={() => auth.logOut()} className="btn-submit">
                                Выход
                            </Button>
                        </Form.Item>
                    </Form.Item>
                </Form>
            </div>
        </div>
    );
};

export default PdfDragAndDropUploader;
