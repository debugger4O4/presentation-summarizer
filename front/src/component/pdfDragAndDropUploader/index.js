import React, {useState} from 'react';
import pdfToText from 'react-pdftotext';
import './index.css';
import {InboxOutlined} from '@ant-design/icons';
import {Form, Upload} from 'antd';
import axios from 'axios';

const normFile = e => {
    console.log('Upload event:', e);
    if (Array.isArray(e)) {
        return e;
    }
    return e && e.fileList;
};

const PdfDragAndDropUploader = () => {

    const [textForSummarize, setTextForSummarize] = useState('');
    const [loading, setLoading] = useState(false);

    const handleFileChange = async ({fileList}) => {
        // Не отправлять запрос, если уже идет один.
        if (loading) {
            return;
        }
        setLoading(true);
        const promises = fileList.map(file => {
            if (!file.originFileObj) {
                return null;
            }
            return pdfToText(file.originFileObj);
        });
        const texts = await Promise.allSettled(promises.filter(Boolean));
        let content = '';
        texts.forEach(result => {
            if (result.status === 'fulfilled') {
                content += result.value + ' ';
            }
        });
        setTextForSummarize(content)

        try {
            const response = await axios({
                url: 'http://localhost:8080/summarize/getSummarize',
                method: 'POST',
                data: {textForSummarize},
                responseType: 'blob', // Тип ответа: бинарный файл.
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            // Автоматическое открытие окна скачивания.
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'presentation.pptx');
            document.body.appendChild(link);
            link.click();
        } catch (error) {
            console.error('Error when receiving the file:', error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <div className="upload-center-container">
                <Form.Item>
                    <Form.Item name="dragger" valuePropName="fileList" getValueFromEvent={normFile} noStyle>
                        <Upload.Dragger name="files" onChange={handleFileChange} multiple showUploadList={false}>
                            <p className="ant-upload-drag-icon"><InboxOutlined/></p>
                            <p className="ant-upload-text">Щелкните или перетащите файл в эту область для загрузки</p>
                            <p className="ant-upload-hint">Поддержка загрузки одного или нескольких файлов.</p>
                        </Upload.Dragger>
                    </Form.Item>
                </Form.Item>
            </div>
        </div>
    );
};

export default PdfDragAndDropUploader;
