#= require form.coffee

class UpdateUserView extends UserForm
  template: JST['user_edit']
  name: 'inner-view'
  events:
    'click #save': 'update'
  model_attributes: [ 'email', 'first_name', 'last_name', 'phone', 'location', 'about' ]

  render: () ->
    @$el.html @template user: application.current_user
    super

  validate_password_length: () ->
    if @is_editing_password()
      super
    else
      true

  validate_password_confirmation: () ->
    if @is_editing_password()
      super
    else
      true

  update: () ->
    @trigger 'update', @ if @validate()

  get_attributes: () ->
    attributes = {}
    for attribute in @model_attributes
      attributes[attribute] = @$("[name='#{attribute}']").val()
    if @is_editing_password()
      attributes.password = @$("[name='password']").val()
    attributes