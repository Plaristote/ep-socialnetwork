class UserCollection extends Collection
  model: User
  url: "/users"
  resourceName: 'users'

  parse: (response) ->
    @state.lastPage = @state.currentPage if response.users.length == 0
    response.users