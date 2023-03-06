import{s as a,i as r,y as p,p as u,c as g,d as m,f}from"./indexhtml-cc9636d8.js";/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const v=e=>t=>typeof t=="function"?((o,i)=>(customElements.define(o,i),i))(e,t):((o,i)=>{const{kind:s,elements:n}=i;return{kind:s,elements:n,finisher(c){customElements.define(o,c)}}})(e,t);function y(e){var o;const t=[];for(;e&&e.parentNode;){const i=w(e);if(i.nodeId!==-1){if((o=i.element)!=null&&o.tagName.startsWith("FLOW-CONTAINER-"))break;t.push(i)}e=e.parentElement?e.parentElement:e.parentNode.host}return t.reverse()}function w(e){const t=window.Vaadin;if(t&&t.Flow){const{clients:o}=t.Flow,i=Object.keys(o);for(const s of i){const n=o[s];if(n.getNodeId){const c=n.getNodeId(e);if(c>=0)return{nodeId:c,uiId:n.getUIId(),element:e}}}}return{nodeId:-1,uiId:-1,element:void 0}}var k=Object.defineProperty,E=Object.getOwnPropertyDescriptor,C=(e,t,o,i)=>{for(var s=i>1?void 0:i?E(t,o):t,n=e.length-1,c;n>=0;n--)(c=e[n])&&(s=(i?c(t,o,s):c(s))||s);return i&&s&&k(t,o,s),s};let l=class extends a{render(){return p`<div
      tabindex="-1"
      @mousemove=${this.onMouseMove}
      @click=${this.onClick}
      @keydown=${this.onKeyDown}
    ></div>`}onClick(e){const t=this.getTargetElement(e);this.dispatchEvent(new CustomEvent("shim-click",{detail:{target:t}}))}onMouseMove(e){const t=this.getTargetElement(e);this.dispatchEvent(new CustomEvent("shim-mousemove",{detail:{target:t}}))}onKeyDown(e){this.dispatchEvent(new CustomEvent("shim-keydown",{detail:{originalEvent:e}}))}getTargetElement(e){this.style.display="none";const t=document.elementFromPoint(e.clientX,e.clientY);return this.style.display="",t}};l.shadowRootOptions={...a.shadowRootOptions,delegatesFocus:!0};l.styles=[r`
      div {
        pointer-events: auto;
        background: rgba(255, 255, 255, 0);
        position: fixed;
        inset: 0px;
        z-index: 1000000;
      }
    `];l=C([v("vaadin-dev-tools-shim")],l);var b=Object.defineProperty,I=Object.getOwnPropertyDescriptor,d=(e,t,o,i)=>{for(var s=i>1?void 0:i?I(t,o):t,n=e.length-1,c;n>=0;n--)(c=e[n])&&(s=(i?c(t,o,s):c(s))||s);return i&&s&&b(t,o,s),s};let h=class extends a{constructor(){super(...arguments),this.active=!1,this.components=[],this.selected=0}connectedCallback(){super.connectedCallback();const e=new CSSStyleSheet;e.replaceSync(`
    .vaadin-dev-tools-highlight {
      outline: 1px solid red
    }`),document.adoptedStyleSheets=[...document.adoptedStyleSheets,e]}render(){return this.style.display=this.active?"block":"none",p`
      <vaadin-dev-tools-shim
        @shim-click=${this.shimClick}
        @shim-mousemove=${this.shimMove}
        @shim-keydown=${this.shimKeydown}
      ></vaadin-dev-tools-shim>
      <div class="window popup component-picker-info">
        <div>
          <h3>Locate a component in source code</h3>
          <p>Use the mouse cursor to highligh components in the UI.</p>
          <p>Use arrow down/up to cycle through and highlight specific components under the cursor.</p>
          <p>
            Click the primary mouse button to open the corresponding source code line of the highlighted component in
            your IDE.
          </p>
        </div>
      </div>
      <div class="window popup component-picker-components-info">
        <div>
          ${this.components.map((e,t)=>p`<div class=${t===this.selected?"selected":""}>
                ${e.element.tagName.toLowerCase()}
              </div>`)}
        </div>
      </div>
    `}update(e){var t;if(super.update(e),(e.has("selected")||e.has("components"))&&this.highlight((t=this.components[this.selected])==null?void 0:t.element),e.has("active")){const o=e.get("active"),i=this.active;!o&&i?requestAnimationFrame(()=>this.shim.focus()):o&&!i&&this.highlight(void 0)}}shimKeydown(e){const t=e.detail.originalEvent;if(t.key==="Escape")this.abort(),e.stopPropagation(),e.preventDefault();else if(t.key==="ArrowUp"){let o=this.selected-1;o<0&&(o=this.components.length-1),this.selected=o}else t.key==="ArrowDown"?this.selected=(this.selected+1)%this.components.length:t.key==="Enter"&&(this.pickSelectedComponent(),e.stopPropagation(),e.preventDefault())}shimMove(e){const t=e.detail.target;this.components=y(t),this.selected=this.components.length-1}shimClick(e){this.pickSelectedComponent()}abort(){this.dispatchEvent(new CustomEvent("component-picker-abort",{}))}pickSelectedComponent(){const e=this.components[this.selected];if(!e){this.abort();return}this.dispatchEvent(new CustomEvent("component-picker-pick",{detail:{component:{nodeId:e.nodeId,uiId:e.uiId}}}))}highlight(e){this.highlighted&&this.highlighted.classList.remove("vaadin-dev-tools-highlight"),this.highlighted=e,this.highlighted&&this.highlighted.classList.add("vaadin-dev-tools-highlight")}};h.styles=[u,r`
      .component-picker-info {
        left: 1em;
        bottom: 1em;
      }

      .component-picker-components-info {
        right: 3em;
        bottom: 1em;
      }

      .component-picker-components-info .selected {
        font-weight: bold;
      }
    `];d([g({type:Boolean})],h.prototype,"active",2);d([m()],h.prototype,"components",2);d([m()],h.prototype,"selected",2);d([f("vaadin-dev-tools-shim")],h.prototype,"shim",2);h=d([v("vaadin-dev-tools-component-picker")],h);export{h as ComponentPicker};
