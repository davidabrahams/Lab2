## Synopsis

This project is my submission for the second Mobile Prototyping Lab. It is an app that allows the user to search the Web for an image, and then save selected images to a "feed". These images are stored in a SQLlite DB, and can be viewed in Feed window.

## Features and Usability

* This app implements a `FragmentStatePagerAdapter`. The default page is the Search page. The user can swipe right in order to view their feed.
* The photos in the search are shown in a GridView. The number of columnns is set based on the size of the screen. Currently, the app shows the top 30 search results.
* The app's data persists even when the phone is flipped. Additionally, the number of columns will resize.
* In order to add a photo to the "Feed", long-click on an image. This will prompt the user with a dialog confirming the addition.
* On the feed page, press the "Next" and "Previous" buttons to view another image.
* To delete an image from the feed, long-click the image in the "Feed" page and answer the dialog.
* This page also supports flipping orientations.
* The feed persists in an SQLdb across launches.

## Dependencies

I used the Volley external library.
