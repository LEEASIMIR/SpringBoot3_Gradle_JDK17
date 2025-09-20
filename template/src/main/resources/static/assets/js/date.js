/**
 * 날짜 포맷팅 자바스크립트 클래스
 * @author 이봉용
 * @date 25. 9. 20.
 */
class CustomDate {
    constructor(date) {
        this.date = date ? date : new Date();
    }

    /**
     * @param formatStr yyyy-MM-dd HH:mm:ss.SSS
     * @author 이봉용
     * @date 25. 9. 20.
     */
    format(formatStr) {
        formatStr = formatStr.replaceAll('yyyy', this.date.getFullYear());
        formatStr = formatStr.replaceAll('MM', String(this.date.getMonth()+1).padStart(2, '0'));
        formatStr = formatStr.replaceAll('dd', String(this.date.getDate()).padStart(2, '0'));
        formatStr = formatStr.replaceAll('HH', String(this.date.getHours()).padStart(2, '0'));
        formatStr = formatStr.replaceAll('mm', String(this.date.getMinutes()).padStart(2, '0'));
        formatStr = formatStr.replaceAll('ss', String(this.date.getSeconds()).padStart(2, '0'));
        formatStr = formatStr.replaceAll('SSS', String(this.date.getMilliseconds()).padStart(3, '0'));
        return formatStr;
    }
}

const dateFormatSymbol = Object.freeze({
    YY: 'YY',
    DOWN: 'DOWN',
    LEFT: 'LEFT',
    RIGHT: 'RIGHT'
});

