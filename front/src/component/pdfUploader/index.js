import React, {useState} from 'react';
import pdfToText from 'react-pdftotext';
import './index.css';


const PdfUploaderComponent = () => {

    const [textForSummarize, setTextForSummarize] = useState('');

    const handleFileChange = async (event) => {
        const files = Array.from(event.target.files);
        const promises = [];

        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            if (file) {
                promises.push(pdfToText(file));
            }
        }

        try {
            const texts = await Promise.all(promises);
            const combinedString = texts.join(' ').trim();
            setTextForSummarize(combinedString);
        } catch (error) {
            console.error("Error extracting text from PDFs:", error);
        }
    };

    return (
        <div className="file-input">
            <input id="file" type="file" className="file" onChange={handleFileChange} multiple />
            <label htmlFor="file">
                Выберите PDF-файлы
            </label>
            <div>{textForSummarize}</div>
        </div>

    );
};

export default PdfUploaderComponent;