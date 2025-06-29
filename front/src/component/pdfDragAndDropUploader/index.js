import React, {useState} from 'react';
import pdfToText from 'react-pdftotext';
import './index.css';
import {InboxOutlined} from '@ant-design/icons';
import {Button, Form, Upload} from 'antd';
import axios from 'axios';

const PdfDragAndDropUploader = () => {

    const [textForSummarize, setTextForSummarize] = useState('');
    const [downloading, setDownloading] = useState(false);

    const handleFileChange = async ({fileList}) => {
        if (downloading) {
            return;
        }
        setDownloading(true);
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
        setDownloading(false);
        setTextForSummarize(content);
    };

    const downloadPresentation = async () => {
        setDownloading(true);
        try {
            const response = await axios({
                url: 'http://localhost:8080/summarize/getSummarize',
                method: 'POST',
                data: {textForSummarize},
                responseType: 'blob', // Важно указать blob для бинарных данных
            });

            // Создаем ссылку для скачивания файла
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'summarize.pptx');
            document.body.appendChild(link);
            link.click();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error('Ошибка при загрузке презентации:', error);
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

    return (
        <div>
            <div className="upload-center-container">
                <Form.Item>
                    <Form.Item name="dragger" valuePropName="fileList" getValueFromEvent={normFile} noStyle>
                        <Upload.Dragger name="files" onChange={handleFileChange} multiple>
                            <p className="ant-upload-drag-icon"><InboxOutlined/></p>
                            <p className="ant-upload-text">Щелкните или перетащите файл в эту область для загрузки</p>
                            <p className="ant-upload-hint">Поддержка загрузки одного или нескольких файлов.</p>
                        </Upload.Dragger>
                    </Form.Item>
                    <br/>
                    <br/>
                    <br/>
                    <Form.Item style={{ display: 'flex', justifyContent: 'center' }}>
                        <Button  type={"primary"} disabled={downloading} onClick={downloadPresentation}>
                            Суммаризировать презентации
                        </Button>
                    </Form.Item>
                </Form.Item>
            </div>
            <div>

            </div>
        </div>
    );
};

export default PdfDragAndDropUploader;
