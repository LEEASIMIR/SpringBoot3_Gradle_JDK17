class ChunkUploader {
    constructor(url, file, uploadPath='', chunkSize=5 * 1024 * 1024) {
        this.uploadUrl = url;
        this.uploadFile = file;
        this.uploadPath = uploadPath;
        this.chunk = null;
        this.uploadedBytes = 0;
        this.chunkSize = chunkSize;
        this.uploadedListener = {};
    }

    /**
     * @typedef {object} uploadEvent 업로드 이벤트 객체
     * @property {'start'|'uploading'|'error'|'complete'} state 상태
     * @property {boolean} isComplete 업로드 완료 여부
     * @property {number} uploadedBytes 업로드 된 바이트
     * @property {number} fileSize 총 파일 바이트
     * @property {object} res 서버응답 객체
     */

    /**
     * @param {function(uploadEvent): void} callback
     * @author 이봉용
     * @date 25. 9. 21.
     */
    onUploaded(callback) {
        this.uploadedListener.callback = callback;
    }

    async upload() {
        let result = {};
        this.uploadedBytes = 0;

        this.uploadedListener.callback({
            state: 'start',
            isComplete: false,
            uploadedBytes: this.uploadedBytes,
            fileSize: this.uploadFile.size,
            res: null
        });

        while (true) {
            this.chunk = this.uploadFile.slice(this.uploadedBytes, this.uploadedBytes + this.chunkSize);

            const response = await fetch(this.uploadUrl, {
                method: 'POST',
                headers: {
                    'BONG-File-Range': `${this.uploadedBytes}/${this.uploadFile.size}`,
                    'BONG-File-Type': this.uploadFile.type,
                    'BONG-File-Name': this.uploadFile.name,
                    'BONG-File-Path': this.uploadPath,
                    'BONG-Chunk-Size': this.chunk.size
                },
                body: this.chunk
            });

            if (response.status !== 200 && response.status !== 308) {
                this.uploadedListener.callback({
                    state: 'error',
                    isComplete: false,
                    uploadedBytes: this.uploadedBytes,
                    fileSize: this.uploadFile.size,
                    res: await response.json()
                });
                throw new Error('청크 업로드 실패');
            }

            this.uploadedBytes += this.chunk.size;

            this.uploadedListener.callback({
                state: 'uploading',
                isComplete: false,
                uploadedBytes: this.uploadedBytes,
                fileSize: this.uploadFile.size,
                res: null
            });

            if(response.status === 308) continue;

            if(response.status === 200) {
                result = await response.json();
                this.uploadedListener.callback({
                    state: 'complete',
                    isComplete: true,
                    uploadedBytes: this.uploadedBytes,
                    fileSize: this.uploadFile.size,
                    res: result
                });
                break;
            }
        }
        return result;
    }

}