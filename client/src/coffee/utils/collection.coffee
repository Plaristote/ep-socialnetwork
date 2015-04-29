class Collection extends Backbone.PageableCollection
  state:
    firstPage: 0
    pageSize: 10
  queryParams:
    currentPage: "Page"
    pageSize:    "Limit"

  parse: (response) ->
    data = if @resourceName? then response[@resourceName] else response
    @state.lastPage = @state.currentPage if data.length == 0
    data

  is_at_last_page: () ->
    @state.lastPage == @state.currentPage