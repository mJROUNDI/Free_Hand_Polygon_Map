Free_Hand_Polygon_Map
=====================

Allows Free hand drawing of polygons on MapFragments and optionaly reduce number of points using Douglas-Peucker algorithm

Documentation
=============

To plug the drawer in your MapFragment
``` java
  FreeHandDrawer mFreeHandDrawer = new FreeHandDrawer.Builder(mapFragment).build();
``` 
To use DouglasPeucker algorithm to reduce your polygon's points number
simply set tolerance (needed by the algorithm) 
the default value is 0 witch means that the polygon will not be reduced
``` java
  FreeHandDrawer mFreeHandDrawer = new FreeHandDrawer.Builder(mapFragment)
                                            .tolerance(10.0)
                                            .build();
``` 
To get the polygon drawn by user you just have to set OnPolygonDrawListener to the FreeHandDrawer

``` java
  mFreeHandDrawer.setOnPolygonDrawListener(new OnPolygonDrawListener() {
            @Override
            public void OnDraw(Polygon polygon) {
                //Handle polygon here
            }
        });
``` 
To customize more your drawer
``` java

        mFreeHandDrawer = new FreeHandDrawer
                          .Builder(mapFragment)
                          .tolerance(0.0)//tolerance needed to reduce number of points default value 0.0 (no reduction)
                          .fillColor(0x220000FF)//color used to fill your polygon 0x220000FF is the default value
                          .lockZoomWhenDrawing(false)//when true disable zooming of your MapFragment (Only in draw mode)
                          .strokeColor(Color.BLUE)///color stroke for polygon 0xFF0000FF is the default value
                          .strokeWidth(2)//stroke width of the polygon 2 is ths default value
                          .build();
``` 

Licence
=======

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
