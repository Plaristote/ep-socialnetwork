class UserForm extends View
  is_editing_password: () ->
    @$("[name='password']").val().length > 0

  validate_failure: (field_name, error) ->
    $error_message = $("<span class='validate-error'>#{error}</span>")
    @$("[name='#{field_name}']").parent().append $error_message

  validate_email: () ->
    regexp = new RegExp "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
    if (@$("[name='email']").val().match regexp) == null
      @validate_failure 'email', 'Not a valid email'
      false
    else
      true

  validate_password_length: () ->
    if @$("[name='password']").val().length < 4
      @validate_failure 'password', 'Minimum length of 4 characters'
      false
    else
      true

  validate_password_confirmation: () ->
    if @$("[name='password']").val() != @$("[name='password_confirm']").val()
      @validate_failure 'password_confirm', 'Password mismatch'
      false
    else
      true

  validate_password: () ->
    @validate_password_length() && @validate_password_confirmation()

  validate_required_field: (field_name) ->
    if @$("[name='#{field_name}']").val().length < 1
      @validate_failure field_name, 'Required field'
      false
    else
      true

  validate_name: () ->
    @validate_required_field('first_name') && @validate_required_field('last_name')

  validate: () ->
    @$(".validate-error").detach()
    @validate_email() && @validate_name() && @validate_password()