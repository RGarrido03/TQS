"use client";

import TripCard from "@/components/TripCard";
import {
  deleteReservation,
  getReservation,
} from "@/service/reservationService";
import { getTrip } from "@/service/tripService";
import { getUserReservations } from "@/service/userService";
import { Currency } from "@/types/currency";
import { Reservation } from "@/types/reservation";
import { Trip } from "@/types/trip";
import { User } from "@/types/user";
import { Button, Chip, Skeleton, Spinner } from "@nextui-org/react";
import { useQuery } from "@tanstack/react-query";
import { useCookies } from "next-client-cookies";
import { useRouter } from "next/navigation";
import { MaterialSymbol } from "react-material-symbols";

export default function Success() {
  const cookies = useCookies();
  const user = JSON.parse(cookies.get("user")!) as User;

  const {
    isPending: isReservationsPending,
    data: reservations,
    refetch,
  } = useQuery<Reservation[]>({
    queryKey: ["reservations", user.id],
    queryFn: () => getUserReservations(user.id),
  });

  return (
    <div className="flex flex-col gap-8">
      <div className="p-8 lg:p-24 flex flex-col items-center justify-center gap-8">
        <h1 className="font-bold text-4xl lg:text-6xl text-center text-balance">
          Check your reservations.
        </h1>
      </div>
      <div className="flex flex-col px-4 gap-8">
        {isReservationsPending ? (
          <div className="flex flex-col gap-4 items-center">
            <Spinner />
            <p>Loading reservations...</p>
          </div>
        ) : reservations!.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 px-4 md:px-8 lg:px-16 gap-8">
            {reservations?.map((reservation) => (
              <div key={reservation.id} className="flex flex-col gap-4">
                <TripCard isLoaded trip={reservation.trip} />
                <div className="flex flex-row justify-between items-center">
                  <Chip>
                    {reservation.seats} seat{reservation.seats > 1 && "s"}{" "}
                    reserved
                  </Chip>
                  <Button
                    color="danger"
                    onClick={async () => {
                      await deleteReservation(reservation.id);
                      refetch();
                    }}
                  >
                    Cancel
                  </Button>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p className="text-center">No reservations found. 🥲</p>
        )}
      </div>
    </div>
  );
}
