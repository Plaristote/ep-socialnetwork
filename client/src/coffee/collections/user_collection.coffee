class UserCollection extends Backbone.PageableCollection
  model: User
  url: "/users"
  state:
    firstPage: 0
  queryParams:
    currentPage: "Page"
    pageSize:    "Limit"
        
  parse: (response) ->
    response.users