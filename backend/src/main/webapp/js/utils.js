/**
 * Created by lua on 26/10/2015.
 */

/**
 * Reference http://stackoverflow.com/questions/6466135/adding-extra-zeros-in-front-of-a-number-using-jquery
 * @param str
 * @param max
 * @returns {*}
 */
function addZero (str, max) {
    str = str.toString();
    var len = str.length;
    while (len < max) {
        str = "0" + str;
        len++;
    }
    return str;
}