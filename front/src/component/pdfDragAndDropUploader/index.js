import React, {useState} from 'react';
import pdfToText from 'react-pdftotext';
import './index.css';
import { InboxOutlined } from '@ant-design/icons';
import { Form, Upload } from 'antd';

const normFile = e => {
    console.log('Upload event:', e);
    if (Array.isArray(e)) {
        return e;
    }
    return e && e.fileList;
};

const PdfDragAndDropUploader = () => {

    const [loading, setLoading] = useState(false);

    const handleFileChange = async ({ fileList }) => {
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

        try {
            const texts = await Promise.allSettled(promises.filter(Boolean));

            let textForSummarize = '';
            texts.forEach(result => {
                if (result.status === 'fulfilled') {
                    textForSummarize += result.value + ' ';
                }
            });
            fetch('http://localhost:8080/summarize/getSummarize', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(textForSummarize.trim())
            })
                .then(response => response.json())
                .then(data => console.log(data))
                .catch(error => console.error('Request execution error:', error));
        } catch (error) {
            console.error('Error extracting text from PDFs:', error);
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
                            <p className="ant-upload-drag-icon"><InboxOutlined /></p>
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
