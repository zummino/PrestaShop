/**
 * Copyright since 2007 PrestaShop SA and Contributors
 * PrestaShop is an International Registered Trademark & Property of PrestaShop SA
 *
 * NOTICE OF LICENSE
 *
 * This source file is subject to the Open Software License (OSL 3.0)
 * that is bundled with this package in the file LICENSE.md.
 * It is also available through the world-wide-web at this URL:
 * https://opensource.org/licenses/OSL-3.0
 * If you did not receive a copy of the license and are unable to
 * obtain it through the world-wide-web, please send an email
 * to license@prestashop.com so we can send you a copy immediately.
 *
 * DISCLAIMER
 *
 * Do not edit or add to this file if you wish to upgrade PrestaShop to newer
 * versions in the future. If you wish to customize PrestaShop for your
 * needs please refer to https://devdocs.prestashop.com/ for more information.
 *
 * @author    PrestaShop SA and Contributors <contact@prestashop.com>
 * @copyright Since 2007 PrestaShop SA and Contributors
 * @license   https://opensource.org/licenses/OSL-3.0 Open Software License (OSL 3.0)
 */

import {isUndefined} from '@PSTypes/typeguard';

const {$} = window;

/**
 * @param {string} disablingInputSelector - selector of input (e.g. checkbox or radio)
 *                 which on change enables/disables the element selected by targetSelector.
 * @param {string} matchingValue - value which should match with disablingInput value to enable/disable related element
 * @param {string} targetSelector - selector of element which is toggled by the disablingInput.
 * @param {boolean} disableOnMatch - once disablingInput & matchingValue values match, then
 *                  if true - related element is disabled
 *                  if false - related element is enabled.
 *
 * Important Note: the component can be configured on construction via the parameters object, but its behaviour
 * and parameters will be overridden if a data attribute is associated to the selector node.
 */
export type FormFieldDisablerParams = {
  disablingInputSelector: string,
  matchingValue: string | null,
  targetSelector: string | null,
  switchEvent: string | null,
  disableOnMatch: boolean,
}
export type InputFormFieldDisablerParams = Partial<FormFieldDisablerParams> & {
  disablingInputSelector: string,
};

/**
 * Enables or disables element depending on certain input value.
 */
export default class FormFieldDisabler {
  params: FormFieldDisablerParams;

  /**
   * @param {InputFormFieldDisablerParams} inputParams
   */
  constructor(inputParams: InputFormFieldDisablerParams) {
    this.params = {
      matchingValue: '0',
      disableOnMatch: true,
      targetSelector: null,
      switchEvent: null,
      ...inputParams,
    };

    this.init();
  }

  private init(): void {
    const disablingInputs: NodeListOf<HTMLInputElement> = document.querySelectorAll(this.params.disablingInputSelector);
    disablingInputs.forEach((input: HTMLInputElement) => {
      this.updateTargetState(input);

      $(input).on('change', () => {
        this.updateTargetState(input);
      });
    });
  }

  private updateTargetState(inputElement: HTMLInputElement): void {
    const toggleValue = this.getInputValue(inputElement);

    if (isUndefined(toggleValue)) {
      return;
    }

    const matchingValue = inputElement.dataset.matchingValue ?? this.params.matchingValue;
    const targetSelector = inputElement.dataset.targetSelector ?? this.params.targetSelector;
    const switchEvent = inputElement.dataset.switchEvent ?? this.params.switchEvent;
    const disableOnMatch = inputElement.dataset?.disableOnMatch === '1' ?? this.params.disableOnMatch;

    if (matchingValue === null) {
      console.error('No matching value defined for inputElement', inputElement);
      return;
    }

    if (targetSelector === null) {
      console.error('No target selector defined for inputElement', inputElement);
      return;
    }
    let disabledState;

    if (toggleValue === matchingValue) {
      disabledState = disableOnMatch;
    } else {
      disabledState = !disableOnMatch;
    }

    this.toggle(targetSelector, disabledState, switchEvent);
  }

  private getInputValue(inputElement: HTMLInputElement): string | undefined {
    switch (inputElement.type) {
      case 'radio': {
        const checkedRadios = document.querySelectorAll<HTMLInputElement>(`[name="${inputElement.name}"]`);
        let checkedValue: string | undefined;
        checkedRadios.forEach((radio: HTMLInputElement) => {
          if (radio.checked) {
            checkedValue = radio.value;
          }
        });

        return checkedValue;
      }
      case 'checkbox':
        return inputElement.checked ? inputElement.value : undefined;
      default:
        return inputElement.value;
    }
  }

  private toggle(targetSelector: string, disable: boolean, switchEvent: string | null): void {
    if (switchEvent) {
      const {eventEmitter} = window.prestashop.instance;

      if (!eventEmitter) {
        console.error('Trying to use EventEmitter without having initialised the component before.');
      } else {
        eventEmitter.emit(switchEvent, {targetSelector, disable});
      }
    }

    const elementsToToggle: NodeListOf<Element> = document.querySelectorAll(targetSelector);

    if (elementsToToggle.length === 0) {
      console.error(`Could not find target ${targetSelector}`);
      return;
    }

    elementsToToggle.forEach((elementToToggle: Element) => {
      elementToToggle.classList.toggle('disabled', disable);
      elementToToggle.toggleAttribute('disabled', disable);

      const formElements = elementToToggle.querySelectorAll('input, select, textarea, button, option, fieldset');

      if (formElements.length === 0) {
        return;
      }

      formElements.forEach((element: Element) => {
        element.toggleAttribute('disabled', disable);
      });
    });
  }
}
