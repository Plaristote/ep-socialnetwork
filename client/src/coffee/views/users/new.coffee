#= require form.coffee

class NewUserView extends UserForm
  template: JST['user_new']
  name: 'inner-view'
  events:
    'click #subscribe': 'subscribe'
  model_attributes: [ 'email', 'first_name', 'last_name', 'password', 'phone', 'location', 'about' ]

  render: () ->
    @$el.append @template()
    super

  subscribe: () ->
    @trigger 'subscribe', @ if @validate()

  on_subscribe_failure: (errors) ->
    alert 'subscribtion failed'

  get_attributes: () ->
    attributes = {}
    for attribute in @model_attributes
      attributes[attribute] = @$("[name='#{attribute}']").val()
    attributes