window.org_ikernits_sample_vaadin_components_HighChart = function () {

    var self = this;
    this.value = 1;

    this.count = function() {
        self.test2(self.value);
        self.value = self.value + 1;
        console.debug("value = " + self.value);
    }

    this.onStateChange = function () {
        var domId = this.getState().domId;
        var optionsJson = this.getState().optionsJson;
        if (optionsJson) {
            eval("var options = " + optionsJson + ";");
            $('#' + domId).highcharts(options);
        }
        self.count();
    };

    this.test1 = function (arg) {
        console.debug("test1: " + arg);
    }
};
