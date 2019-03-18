window.org_test_TypeExtension = function () {
    var element = this.getElement(this.getParentId());
    var self = this;
    this.onStateChange = function () {
        if (self.getState().type == "") {
            element.setAttribute("type", "text")
        } else {
            element.setAttribute("type", self.getState().type);
        }
    };
    this.onUnregister = function() {
        element.setAttribute("type", "text")
    }
};
