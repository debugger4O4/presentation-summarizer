import React, { useState } from 'react';
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
    const [textForSummarize, setTextForSummarize] = useState('');

    const handleFileChange = async ({ fileList }) => {
        const promises = fileList.map(file => {
            if (!file.originFileObj) return null;
            return pdfToText(file.originFileObj);
        });

        try {
            const texts = await Promise.allSettled(promises.filter(Boolean));

            let combinedString = '';
            texts.forEach(result => {
                if (result.status === 'fulfilled') {
                    combinedString += result.value + ' ';
                }
            });

            setTextForSummarize(combinedString.trim());
        } catch (error) {
            console.error("Error extracting text from PDFs:", error);
        }
    };

    return (
        <div>
            <Form.Item>
                <Form.Item name="dragger" valuePropName="fileList" getValueFromEvent={normFile} noStyle>
                    <Upload.Dragger name="files" onChange={handleFileChange} multiple>
                        <p className="ant-upload-drag-icon"><InboxOutlined /></p>
                        <p className="ant-upload-text">Щелкните или перетащите файл в эту область для загрузки</p>
                        <p className="ant-upload-hint">Поддержка загрузки одного или нескольких файлов.</p>
                    </Upload.Dragger>
                </Form.Item>
            </Form.Item>

            <div>{textForSummarize}</div>
        </div>
    );
};

export default PdfDragAndDropUploader;
