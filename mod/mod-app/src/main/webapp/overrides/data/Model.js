/**
 * overrides.data.Model
 *
 * @author mixam
 * @date 06/09/2019
 */
Ext.define('overrides.data.Model', {
    override: 'Ext.data.Model',
    requires: [],
    setData(data) {
        this.data = data;
        Ext.each(this.fields, function (f) {
            this.data[f.name] = f.convert ? f.convert(data[f.name]) : data[f.name];
        }, this);

        const id = this.get(this.idProperty);
        if (id) this.id = id;
        else this.data[this.idProperty] = this.id;

        this.phantom = !(this.id > 0);
        this.callJoined('afterCommit');
    },
    applyResponse(resp) {
        if (resp.responseText) {
            resp = resp.responseObject || Ext.decode(resp.responseText);
        }
        if (resp.metaData) {
            resp = resp[resp.metaData.root];
            if (Ext.isArray(resp)) resp = resp[0];
        }
        this.setData(resp);
    }
});
