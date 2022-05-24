
//*[contains(@class, 'x-test-hook-36')]//*[contains(@class, 'x-test-hook-37') and contains(text(),'val')]

console.log("Selenium: attribute-hooks-locators.js loaded.")
//debugger;


const hookPrefix = 'x-test-hook-';
const templatePrefix = 'x-test-tpl-';
const hookRegex = new RegExp(`^..............(?:(?:${hookPrefix})|(?:${templatePrefix}))\\d+$`);


function myLocatorBuilder(builder) {

    return (elem) => {
        //debugger;
        let locator = null;

        let templateRootParent = false;

        while (elem !== document) {

            let hookName = getAttributeHook(elem);
            if (hookName) {
                //console.warn('Cannot find hook for element', elem)
                //break;
                //throw new Error('Cannot find hook for element', elem)

                const templateRoot = hookName.indexOf(templatePrefix) >= 0;

                const siblings = getAllSiblings(elem, sameHookFilter(hookName));

                const index = siblings.length > 1 ? siblings.indexOf(elem) + 1 : 0;

                if (!locator || siblings.length > 1 || templateRoot || templateRootParent) { //first loop (target element) or more siblings found
                    locator = builder.elementLocator(hookName, index) + (locator || '');
                    templateRootParent = !!templateRoot;
                }
            }
            elem = elem.parentNode;
        }
        //console.log('locator', locator);
        return builder.prefix + locator;
    }
}


const cssBuilders = {
    elementLocator: (hookName, index) => {
        return '[' + hookName + ']' + (index > 0 ? ':nth-of-type(' + index + ')' : '') + ' ';
    },
    prefix: 'css='
}

const xpathBuilders = {
    elementLocator: (hookName, index) => {
        return `//*[@${hookName}]` + (index > 0 ? `[${index}]` : '');
    },
    prefix: ''
}

LocatorBuilders.add('myCss', myLocatorBuilder(cssBuilders));
LocatorBuilders.add('myXPath', myLocatorBuilder(xpathBuilders));

LocatorBuilders.setPreferredOrder(['myXPath', 'myCss']);

function getAttributeHook(elem) {
    for (var i = 0, l = elem.attributes.length; i < l; ++i) {
        let match = elem.attributes[i].name.match(hookRegex);
        if (match) {
            return match[0];
        }
    }
}

// get all sibilings and filter
function getAllSiblings(elem, filter) {
    var siblings = [];
    elem = elem.parentNode.firstChild;
    do {
        if (elem.nodeType === 1) {
            if (!filter || filter(elem)) {
                siblings.push(elem);
            }
        }
    } while ((elem = elem.nextSibling))
    return siblings;
}

function sameHookFilter(hookName) {
    return e => e.hasAttribute(hookName);
}
