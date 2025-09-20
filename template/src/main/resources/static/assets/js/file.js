class FileUtil {
    constructor() {
    }

    chunkUploadEventBus = {
        listeners: {},

        on(eventName, callback=(data={
            isSuccess: false,
            isComplete: false,
            result: {},
            currentIndex: 0,
            totalChunkCnt: 0
        }) => {}) {
            if (!this.listeners[eventName]) {
                this.listeners[eventName] = [];
            }
            this.listeners[eventName].push(callback);
        },

        emit(eventName, data={
            isSuccess: false,
            isComplete: false,
            result: {},
            currentIndex: 0,
            totalChunkCnt: 0
        }) {
            if (this.listeners[eventName]) {
                this.listeners[eventName].forEach(callback => callback(data));
            }
        }
    }

    async upload(file) {
        const formData = new FormData();
        formData.append('file', file);

        const response = await fetch('/file-upload', {method: 'POST', body: formData})
        const json = await response.json();

        if(json.status !== 200) {
            throw new Error(json.message);
        }

        return json;
    }

    async chunkUpload(apiUrl, file) {

        const chunkSize = 1024 * 1024;
        let currentIndex = 0;
        let totalChunkCnt = Math.ceil(file.size / chunkSize);
        let result = {};
        try {
            let tempDirName = new CustomDate().format('yyyyMMddHHmmssSSS');
            for(currentIndex=0;currentIndex < totalChunkCnt;currentIndex++) {
                const start = currentIndex * chunkSize;
                const end = Math.min(start + chunkSize, file.size);
                const chunk = file.slice(start, end);

                const formData = new FormData();
                formData.append('chunk', chunk);
                formData.append('uniqueTempDirName', tempDirName);
                formData.append('originFileName', file.name);
                formData.append('currentIndex', currentIndex);
                formData.append('totalChunkCnt', totalChunkCnt);

                const response = await fetch(apiUrl, {method: 'POST', body: formData})
                const json = await response.json();

                if(json.status !== 200) {
                    throw new Error(json.message);
                }

                this.chunkUploadEventBus.emit('chunkUpload', {
                    isSuccess: true,
                    isComplete: currentIndex+1 >= totalChunkCnt,
                    result: json,
                    currentIndex: currentIndex,
                    totalChunkCnt: totalChunkCnt
                });
            }

        } catch (e) {
            console.error(e);
            this.chunkUploadEventBus.emit('chunkUpload', {
                isSuccess: false,
                isComplete: false,
                result: e,
                currentIndex: currentIndex,
                totalChunkCnt: totalChunkCnt
            });
        }
    }

}